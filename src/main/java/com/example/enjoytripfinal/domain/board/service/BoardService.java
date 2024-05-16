package com.example.enjoytripfinal.domain.board.service;

import com.example.enjoytripfinal.domain.board.dto.request.MakeBoardRequest;
import com.example.enjoytripfinal.domain.board.dto.request.UpdateBoardRequest;
import com.example.enjoytripfinal.domain.board.dto.response.BoardDetailResponse;
import com.example.enjoytripfinal.domain.board.dto.response.BoardResponse;
import com.example.enjoytripfinal.domain.board.entity.Board;
import com.example.enjoytripfinal.domain.board.mapper.BoardCommentMapper;
import com.example.enjoytripfinal.domain.board.repository.BoardRepository;
import com.example.enjoytripfinal.domain.member.entity.Member;
import com.example.enjoytripfinal.domain.member.service.MemberService;
import com.example.enjoytripfinal.global.AuthorityException;
import jakarta.persistence.EntityExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    private final BoardCommentMapper boardCommentMapper;
    private final MemberService memberService;

    public BoardService(BoardRepository boardRepository, BoardCommentMapper boardCommentMapper, MemberService memberService) {
        this.boardRepository = boardRepository;
        this.boardCommentMapper = boardCommentMapper;
        this.memberService = memberService;
    }

    @Transactional
    public BoardResponse makeBoard(MakeBoardRequest request) {
        Board board = boardCommentMapper.dtoToBoardEntity(request);
        Member curMember = memberService.getMemberByJwt();
        board.setMappingMember(curMember);
        boardRepository.save(board);
        return boardCommentMapper.entityToResponse(board);
    }

    public Page<BoardResponse> getPageList(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAllByMember(pageable);

        List<BoardResponse> list = boardPage.getContent()
                .stream().map(boardCommentMapper::entityToResponse).collect(Collectors.toList());

        return new PageImpl<>(list,pageable, boardPage.getTotalElements());
    }

    @Transactional
    public BoardDetailResponse searchBoardById(UUID boardId) {
        Board board = boardRepository.findBoardWithMemberAndCommentListById(boardId);
        board.upViewCount();
        return boardCommentMapper.entityToDetailResponse(board);
    }


    @Transactional
    public BoardResponse updateBoard(UpdateBoardRequest request) {
        Member curMember = memberService.getMemberByJwt();
        Board board = boardRepository.findByIdWithMember(request.getBoardId()).orElseThrow(EntityExistsException::new);

        if(!board.getMember().getId().equals(curMember.getId())) {
            throw new AuthorityException("해당 게시글에 접근 권한이 없습니다.");
        }

        board.updateBoard(request.getTitle(),request.getContent());

        return boardCommentMapper.entityToResponse(board);
    }

    public void deleteBoard(UUID id) {
        boardRepository.deleteById(id);
    }
}
