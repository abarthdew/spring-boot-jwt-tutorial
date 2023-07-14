package com.example.jwttutorial.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jwttutorial.dto.UserDto;
import com.example.jwttutorial.entity.Authority;
import com.example.jwttutorial.entity.User;
import com.example.jwttutorial.repository.UserRepository;
import com.example.util.SecurityUtil;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User signup(UserDto userDto) { // 회원가입
    if (
      userRepository.findOneWithAuthoritiesByUsername( // db에 user 정보 중복 확인
        userDto.getUsername()
      ).orElse(null) != null
    ) {
      throw new RuntimeException("이미 가입되어 있는 유저입니다.");
    }

    // 새로운 user면 권한 정보 만들기
    Authority authority = Authority.builder()
      .authorityName("ROLE_USER") // data.sql의 권한은 USER, ADMIN_ROLE 두 가지임: 권한 검증 부분 테스트 로직
      .build();

    // 권한 정보 포함 User 정보 만들기
    User user = User.builder()
      .username(userDto.getUsername())
      .password(passwordEncoder.encode(userDto.getPassword()))
      .nickname(userDto.getNickname())
      .authorities(Collections.singleton(authority))
      .activated(true)
      .build();

    // User, 권한 정보 저장
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public User getUserWithAuthorities(String username) { // username 기준으로 유저+권한정보 가져옴
    return userRepository.findOneWithAuthoritiesByUsername(username).orElse(null);
  }

  @Transactional(readOnly = true)
  public Optional<User> getMyUserWithAuthorities() { // 현재 SecurityContext에 저장된 username에 해당하는 유저+권한정보만 가져옴
    return SecurityUtil.getCurrentUsername()
      .flatMap(userRepository::findOneWithAuthoritiesByUsername);
  }
}
