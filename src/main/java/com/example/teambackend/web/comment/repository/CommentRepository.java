package com.example.teambackend.web.comment.repository;




import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.comment.domain.Comment;
import com.example.teambackend.web.notice.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    default Comment findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new InvalidCredentialsException("해당 댓글 존재하지 않습니다."));
    }

    Page<Comment> findAllByIsActiveTrueOrderByIdDesc(Pageable pageable);
}