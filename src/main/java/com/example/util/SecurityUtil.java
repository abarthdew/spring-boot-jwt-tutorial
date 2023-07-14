package com.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {

  private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

  private SecurityUtil() {
  }

  // SecurityContext의 Authentication 객체 이용, username 리턴하는 유틸성 메서드
  public static Optional<String> getCurrentUsername() {
    final Authentication authentication
      // Authentication 객체가 저장되는 시점은
      // JwtFilter.java의 doFilter(): SecurityContextHolder.getContext().setAuthentication(authentication); 이 부분
      // ↑ 여기서 저장된(set) Authentication 객체가

      // ↓ 여기로 꺼내지게 됨(get)
      = SecurityContextHolder.getContext().getAuthentication();


    if (authentication == null) {
      logger.debug("Security Context에 인증 정보가 없습니다.");
      return Optional.empty();
    }

    String username = null;
    if (authentication.getPrincipal() instanceof UserDetails) {
      UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
      username = springSecurityUser.getUsername();
    } else if (authentication.getPrincipal() instanceof String) {
      username = (String) authentication.getPrincipal();
    }

    return Optional.ofNullable(username);
  }
}
