package com.example.teambackend.web.board.service;

import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.board.domain.Board;
import com.example.teambackend.web.board.dto.*;
import com.example.teambackend.web.board.repository.BoardRepository;
import com.example.teambackend.web.command.dto.CommandResponseDto;
import com.example.teambackend.web.user.domain.Role;
import com.example.teambackend.web.user.domain.User;
import com.example.teambackend.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.teambackend.common.util.EntityValidator.validateIsNewbie;


@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 작성
    @Transactional
    @Override
    public BoardCreateResponseDto create(BoardCreateRequestDto boardCreateRequestDto, User user,String clientIp) {
        validateIsNewbie(user);
        User FindUser = userRepository.findByIdOrElseThrow(user.getId());

        Board board = Board.builder()
                .title(boardCreateRequestDto.getTitle())
                .content(boardCreateRequestDto.getContent())
                .user(FindUser)
                .isActive(true)
                .ipAddress(clientIp)
                .build();
        boardRepository.save(board);
        return BoardCreateResponseDto.from(board);
    }

    // 게시글 목록 조회
    @Override
    public Page<BoardResponseDto> getAll(Pageable pageable) {
        return boardRepository.findAllByIsActiveTrueOrderByIdDesc(pageable).map(BoardResponseDto::from);
    }
    // 게시글 상세 조회
    @Override
    public BoardResponseDto getById(Long id) {

        Board board = boardRepository.findByIdOrElseThrow(id);
        if (!board.isActive()) {
            throw new InvalidCredentialsException("이미 삭제된 게시물입니다.");
        }
        return BoardResponseDto.from(board);
    }


    // 게시글 수정
    @Override
    @Transactional
    public BoardUpdateResponseDto update(Long id, BoardUpdateRequestDto boardUpdateRequestDto, User user, String clientIp) {

        Board board = boardRepository.findByIdOrElseThrow(id);

        // 작성자만 수정 가능
        if (!board.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("수정 권한이 없습니다.");
        }

        if (!board.isActive()) {
            throw new InvalidCredentialsException("이미 삭제된 게시물입니다.");
        }

        board.updateBoard(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent(),clientIp);

        return BoardUpdateResponseDto.from(board);
    }

    // 게시글 삭제
    @Override
    @Transactional
    public void delete(Long id, User user,String clientIp) {
        validateIsNewbie(user);
        Board board = boardRepository.findByIdOrElseThrow(id);

        // 작성자만 삭제 가능
        if (!board.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("삭제 권한이 없습니다.");
        }
        if (!board.isActive()) {
            throw new InvalidCredentialsException("이미 삭제된 게시물입니다.");
        }
        board.deactivate(clientIp);
    }
}