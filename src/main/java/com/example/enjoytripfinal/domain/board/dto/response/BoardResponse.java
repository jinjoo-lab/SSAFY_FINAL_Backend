package com.example.enjoytripfinal.domain.board.dto.response;

import com.example.enjoytripfinal.domain.member.dto.response.MemberLightResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
    private UUID boardId;
    private String title;
    private String content;
    private Long view;
    private MemberLightResponse member;
}