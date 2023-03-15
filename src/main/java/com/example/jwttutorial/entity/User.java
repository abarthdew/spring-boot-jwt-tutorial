package com.example.jwttutorial.entity;

import lombok.*;
import java.util.Set;
import javax.persistence.*;

@Entity // DB Table 과 1:1로 매핑되는 객체를 뜻
@Table(name = "user") // Table 명: user

// lombok: get, set, builder, constructor 관련 코드 자동 생성
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

   @Id
   @Column(name = "user_id")
   @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
   private Long userId;

   @Column(name = "username", length = 50, unique = true)
   private String username;

   @Column(name = "password", length = 100)
   private String password;

   @Column(name = "nickname", length = 50)
   private String nickname;

   @Column(name = "activated")
   private boolean activated;

   @ManyToMany
   @JoinTable(
      name = "user_authority",
      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
   private Set<Authority> authorities; // 권한 관계
}
