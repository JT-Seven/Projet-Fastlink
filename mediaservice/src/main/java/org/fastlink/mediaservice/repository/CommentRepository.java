package org.fastlink.mediaservice.repository;

import org.fastlink.mediaservice.dto.CommentDto;
import org.fastlink.mediaservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    Optional<List<Comment>> findCommentsByPostId(Long postId);

    Optional<CommentDto> findByCommentId(Long commentId);

    Optional<Comment> findCommentByCommentId(Long commentId);

    Optional<CommentDto> findCommentByPostIdAndCommentId(Long id, Long commentId);
}