package com.example.jwttutorial.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JwtFilter extends GenericFilterBean {

   private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
   public static final String AUTHORIZATION_HEADER = "Authorization";

   private TokenProvider tokenProvider;

   public JwtFilter(TokenProvider tokenProvider) { // 전 단계에서 만든 TokenProvider 주입
      this.tokenProvider = tokenProvider;
   }

   @Override
   // 실제 필터링 로직이 들어가는 메서드
   // : jwt 토큰의 인증정보를 현재 실행중인 SecurityContext에 저장하는 역할
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      String jwt = resolveToken(httpServletRequest); // httpServletRequest에서 토큰 받음
      String requestURI = httpServletRequest.getRequestURI();

      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // validateToken(jwt) 으로 토큰 유효성 검사
         Authentication authentication = tokenProvider.getAuthentication(jwt); // token에서 Authentication 객체를 받아 와 
         SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 set으로 저장
         logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
      } else {
         logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
      }

      filterChain.doFilter(servletRequest, servletResponse);
   }

   // 필터링을 위해 토큰 정보가 있어야 하므로 Request Header에서 토큰 정보를 꺼내올 메서드 추가
   private String resolveToken(HttpServletRequest request) {
      String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // request header에서 token 정보 꺼내옴

      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }

      return null;
   }
}
