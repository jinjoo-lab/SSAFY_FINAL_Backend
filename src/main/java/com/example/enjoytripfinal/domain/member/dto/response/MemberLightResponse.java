package com.example.enjoytripfinal.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLightResponse {
    private UUID memberId;
    private String nickname;
}
