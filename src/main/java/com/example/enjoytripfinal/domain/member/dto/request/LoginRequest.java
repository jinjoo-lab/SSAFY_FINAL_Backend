package com.example.enjoytripfinal.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
