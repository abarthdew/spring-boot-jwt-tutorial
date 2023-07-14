package com.example.jwttutorial.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwttutorial.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { // jpa 사용
  @EntityGraph(attributePaths = "authorities")
    // @EntityGraph: 해당 쿼리를 lazy가 아닌 eager 조회로 authorities 정보를 같이 가져옴
  Optional<User> findOneWithAuthoritiesByUsername(String username);
  // username을 기준으로 User 정보+권한 정보를 가져옴
}
