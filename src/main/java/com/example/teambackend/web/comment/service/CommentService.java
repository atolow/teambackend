package com.example.teambackend.web.comment.service;

import com.example.teambackend.web.comment.dto.*;
import com.example.teambackend.web.user.domain.User;


public interface CommentService {

    // 댓글 생성
    CommentCreateResponseDto createComment(Long boardId, CommentCreateRequestDto commentCreateRequestDto, User currentUser,String clientIp);

    // 댓글 수정
    CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto commentUpdateRequestDto, User currentUser,String clientIp);
    // 댓글 삭제
    void deleteComment(Long commentId, User user,String clientIp);


}