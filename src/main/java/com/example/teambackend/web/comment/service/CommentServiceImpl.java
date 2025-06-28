package com.example.teambackend.web.comment.service;

import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.board.domain.Board;
import com.example.teambackend.web.board.repository.BoardRepository;
import com.example.teambackend.web.comment.domain.Comment;
import com.example.teambackend.web.comment.dto.*;
import com.example.teambackend.web.comment.repository.CommentRepository;
import com.example.teambackend.web.user.domain.Role;
import com.example.teambackend.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.teambackend.web.user.domain.User;

import static com.example.teambackend.common.util.EntityValidator.validateIsNewbie;


@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Override
    public CommentCreateResponseDto createComment(Long boardId, CommentCreateRequestDto dto, User user,String clientIp) {
        validateIsNewbie(user);
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        User FindUser = userRepository.findByIdOrElseThrow(user.getId());

        Comment comment = Comment.builder()
                .user(FindUser)
                .board(board)
                .content(dto.getContent())
                .ipAddress(clientIp)
                .isActive(true)
                .build();

        commentRepository.save(comment);

        return CommentCreateResponseDto.from(comment);
    }

    // 댓글 수정
    @Transactional
    @Override
    public CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto commentUpdateRequestDto, User user,String clientIp) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);


        if (!comment.isActive()) {
            throw new InvalidCredentialsException("이미 삭제된 댓글입니다.");
        }
        // 댓글 작성자가 현재 사용자와 동일한지 확인 (본인만 수정 가능)

        if (!comment.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("수정 권한이 없습니다.");
        }

        // 댓글 내용 업데이트
        comment.updateContent(commentUpdateRequestDto.getContent(),clientIp);

        return CommentUpdateResponseDto.from(comment);
    }

    // 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long commentId, User user,String clientIp) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        if (!comment.isActive()) {
            throw new InvalidCredentialsException("이미 삭제된 댓글입니다.");
        }
        if (!comment.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("삭제 권한이 없습니다.");
        }

        comment.deactivate(clientIp);
    }

}