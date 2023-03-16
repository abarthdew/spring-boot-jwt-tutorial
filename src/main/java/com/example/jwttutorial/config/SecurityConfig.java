package com.example.jwttutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.jwttutorial.jwt.JwtAccessDeniedHandler;
import com.example.jwttutorial.jwt.JwtAuthenticationEntryPoint;
import com.example.jwttutorial.jwt.JwtSecurityConfig;
import com.example.jwttutorial.jwt.TokenProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 메서드 단위로 추가하기 위해 사용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // PasswordEncoder는 BCryptPasswordEncoder사용
        return new BCryptPasswordEncoder();
    }

    // h2-console 하위 모든 요청, 파비콘 관련 요청
    // : security 로직 수행을 하지 않고 접근 가능하게 하는 로직
    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(
              "/h2-console/**"
              ,"/favicon.ico"  
            );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // 토큰 방식을 사용하기 때문에

            .exceptionHandling() // exception을 핸들링 할 때
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 내가 만든 클래스로 적용
            .accessDeniedHandler(jwtAccessDeniedHandler) // 내가 만든 클래스로 적용

            // h2-console를 위한 설정 추가
            .and()
                .headers()
                .frameOptions()
                .sameOrigin()

            // 세션 설정
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않기 때문에 STATELESS로 설정

            .and()
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정
                .antMatchers("/api/hello") // "api/hello"에 대한 요청 api
                .permitAll() // 인증없이 접근 허용
                    // 토큰이 없는 상태에서 요청이 들어오는 api
                    .antMatchers("/api/authenticate").permitAll() // 토큰을 받기 위한 로그인 api
                    .antMatchers("/api/signup").permitAll() // 회원 가입 api
                .anyRequest().authenticated() // 나머지 요청에 대해서는 인증을 받아야 한다는 의미
                
            .and()
                .apply(new JwtSecurityConfig(tokenProvider)); // addFilterBefore로 등록했던 JwtSecurityConfig 적용
    }
}
