package org.fastlink.mediaservice.mapper;

import lombok.AllArgsConstructor;
import org.fastlink.mediaservice.dto.CommentDto;
import org.fastlink.mediaservice.model.Comment;
import org.fastlink.mediaservice.model.Post;
import org.fastlink.mediaservice.services.CommentService;
import org.fastlink.mediaservice.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommentMapper
{

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat();
    private static PostService postService;
    private static CommentService commentService;

    private static MediaMapper mediaMapper;


    public static CommentDto convertEntityToDto(Comment comment)
    {
        DateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(comment.getCommentId());
        commentDto.setDescription(comment.getDescription());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setNbLike(comment.getNbLike());

        commentDto.setParentId(comment.getParentComment().getCommentId());


        commentDto.setMediaList(
                comment.getMediaList()
                        .stream()
                        .map(mediaMapper::convertEntityToDto)
                        .collect(Collectors.toList())
        );

        commentDto.setTagList(
                comment.getTagList()
                        .stream()
                        .map(TagMapper::convertEntityToDto)
                        .collect(Collectors.toList())
        );

        commentDto.setCreationDate(dtFormat.format(comment.getCreationDate()));
        return commentDto;
    }

    public static Comment convertDtoToEntity(CommentDto commentDto)
    {
        Comment comment = new Comment();
        comment.setCommentId(commentDto.getCommentId());
        comment.setDescription(commentDto.getDescription());
        comment.setNbLike(commentDto.getNbLike());
        try
        {
            comment.setCreationDate(dateFormat.parse(commentDto.getCreationDate()));
        } catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date format is not valid");
        }


        comment.setMediaList(
                commentDto.getMediaList()
                        .stream()
                        .map(mediaMapper::convertDtoToEntity)
                        .collect(Collectors.toList())
        );


        comment.setTagList(
                commentDto.getTagList()
                        .stream()
                        .map(TagMapper::convertDtoToEntity)
                        .collect(Collectors.toList())
        );


        Post post = postService.findPostById(commentDto.getPostId());
        comment.setPost(post);

        if (commentDto.getParentId() != null)
        {
            Comment parentComment = commentService.findByCommentId(commentDto.getParentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "parent comment not found"));
            comment.setParentComment(parentComment);
        }


        return comment;
    }
}
