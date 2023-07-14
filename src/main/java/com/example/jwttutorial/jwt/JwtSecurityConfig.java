package com.example.jwttutorial.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TokenProvider와 JwtFilter를 SecurityConfig에 적용할 때 사용
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private TokenProvider tokenProvider;

  public JwtSecurityConfig(TokenProvider tokenProvider) { // 주입
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void configure(HttpSecurity http) {
    // http.addFilterBefore(
    //     new JwtFilter(tokenProvider),
    //     UsernamePasswordAuthenticationFilter.class
    // );

    JwtFilter customFilter = new JwtFilter(tokenProvider); // 이전 단계에서 만든 필터 객체
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class); // 필터를 Security 로직에 등록
  }
}
