package com.example.jwttutorial.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwttutorial.dto.LoginDto;
import com.example.jwttutorial.dto.TokenDto;
import com.example.jwttutorial.jwt.JwtFilter;
import com.example.jwttutorial.jwt.TokenProvider;


@RestController
@RequestMapping("/api")
public class AuthController {
  private final TokenProvider tokenProvider; // 내가 만든 TokenProvider 주입
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
    this.tokenProvider = tokenProvider;
    this.authenticationManagerBuilder = authenticationManagerBuilder; // authenticationManagerBuilder 주입받기
  }

  @PostMapping("/authenticate") // path: [POST]: '/api/authenticate'
  public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) { // LoginDto로 username, password 받음

    UsernamePasswordAuthenticationToken authenticationToken =
      new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
    // 파라미터로 받은 username, password를 통해 UsernamePasswordAuthenticationToken 객체 생성

    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    // authenticate() 실행 시,
    // CustomUserDetailService.java의 loadUserByUsername() 메서드 실행
    // 결과: Authentication 객체 생성
    SecurityContextHolder.getContext().setAuthentication(authentication); // 생성된 Authentication 객체를 SecurityContext에 저장

    String jwt = tokenProvider.createToken(authentication); // 해당 인증 정보(authentication)를 tokenProvider에서 만들었던 createToken() 메서드를 통해 JWT 토큰 생성

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt); // JWT 토큰을 response header에도 넣어주고,

    return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK); // TokenDto를 이용해 response body에도 넣어 리턴
  }
}
