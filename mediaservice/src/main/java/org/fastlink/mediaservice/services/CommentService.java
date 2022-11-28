package org.fastlink.mediaservice.services;

import lombok.AllArgsConstructor;
import org.fastlink.mediaservice.dto.CommentDto;
import org.fastlink.mediaservice.helper.TagHelper;
import org.fastlink.mediaservice.mapper.CommentMapper;
import org.fastlink.mediaservice.model.Comment;
import org.fastlink.mediaservice.model.Tag;
import org.fastlink.mediaservice.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class CommentService
{
    private final CommentRepository commentRepository;

    private final TagHelper tagHelper;

    public Comment saveComment(CommentDto commentDto)
    {
        Comment comment = CommentMapper.convertDtoToEntity(commentDto);

        List<Tag> tagList = tagHelper.identifyTagsFromStringAndMap(comment.getDescription());
        if (tagList != null)
        {
            comment.setTagList(tagList);
            tagHelper.checkForTagsAndAddIfnotFound(comment.getTagList());
        }
        return commentRepository.save(comment);
    }

    public List<CommentDto> getCommentsByPostId(Long postId)
    {
        return commentRepository.findCommentsByPostId(postId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, ""))
                .stream().map(CommentMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllComments()
    {
        return commentRepository.findAll()
                .stream()
                .map(CommentMapper::convertEntityToDto)
                .collect(Collectors.toList()
                );
    }

    public Optional<CommentDto> findCommentById(Long commentId)
    {
        return commentRepository.findByCommentId(commentId);
    }

    /**
     * used in comment mapper to find parent comment
     */
    public Optional<Comment> findByCommentId(Long commentId)
    {
        return commentRepository.findCommentByCommentId(commentId);
    }

    public void deleteComment(CommentDto commentDto)
    {
        commentRepository.delete(CommentMapper.convertDtoToEntity(commentDto));
    }
}
