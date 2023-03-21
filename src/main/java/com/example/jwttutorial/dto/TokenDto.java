package com.example.jwttutorial.dto;

import lombok.*;

// 토큰 정보를 response할 때 사용
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String token;
}
