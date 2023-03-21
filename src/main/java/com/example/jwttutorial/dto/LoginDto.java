package com.example.jwttutorial.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

// Login 시 사용할 DTO
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

   @NotNull
   @Size(min = 3, max = 50) // validation 관련 어노테이션
   private String username;

   @NotNull
   @Size(min = 3, max = 100)
   private String password;
}
