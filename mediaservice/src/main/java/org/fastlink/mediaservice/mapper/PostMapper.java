package org.fastlink.mediaservice.mapper;

import lombok.AllArgsConstructor;
import org.fastlink.mediaservice.dto.PostDto;
import org.fastlink.mediaservice.model.Post;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostMapper
{

    private static MediaMapper mediaMapper;
    public static PostDto convertEntityToDto(Post post)
    {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setUserId(post.getUserId());
        postDto.setContent(post.getContent());
        postDto.setNbLike(post.getNbLike());
        postDto.setNbDislike(post.getNbDislike());

        postDto.setMediaList(
                post.getMediaList()
                        .stream()
                        .map(mediaMapper::convertEntityToDto)
                        .collect(Collectors.toList())
        );

        postDto.setTagList(
                post.getTagList()
                        .stream()
                        .map(TagMapper::convertEntityToDto)
                        .collect(Collectors.toList())
        );

        postDto.setCommentList(
                post.getCommentList()
                        .stream()
                        .map(CommentMapper::convertEntityToDto)
                        .collect(Collectors.toList())
        );

        postDto.setCreationDate(post.getCreationDate());
        return postDto;
    }


    public static Post convertDtoToEntity(PostDto postDto)
    {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setUserId(postDto.getUserId());
        post.setContent(postDto.getContent());
        post.setNbLike(postDto.getNbLike());
        post.setNbDislike(postDto.getNbDislike());
        post.setCreationDate(postDto.getCreationDate());

        post.setMediaList(
                postDto.getMediaList()
                        .stream()
                        .map(mediaMapper::convertDtoToEntity)
                        .collect(Collectors.toList())
        );

        post.setTagList(
                postDto.getTagList()
                        .stream()
                        .map(TagMapper::convertDtoToEntity)
                        .collect(Collectors.toList())
        );

        post.setCommentList(
                postDto.getCommentList()
                        .stream()
                        .map(CommentMapper::convertDtoToEntity)
                        .collect(Collectors.toList())
        );

        return post;
    }
}
