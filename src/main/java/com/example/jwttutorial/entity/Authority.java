package com.example.jwttutorial.entity;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

  @Id
  @Column(name = "authority_name", length = 50)
  private String authorityName; // 권한명: pk
}
