package com.example.jwttutorial.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
            .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정
            .antMatchers("/api/hello") // "api/hello"에 대한 요청은
            .permitAll() // 인증없이 접근 허용
            .anyRequest().authenticated(); // 나머지 요청에 대해서는 인증을 받아야 한다는 의미
    }
}
