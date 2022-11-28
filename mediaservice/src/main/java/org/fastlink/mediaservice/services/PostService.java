package org.fastlink.mediaservice.services;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.fastlink.mediaservice.dto.PostDto;
import org.fastlink.mediaservice.helper.TagHelper;
import org.fastlink.mediaservice.mapper.PostMapper;
import org.fastlink.mediaservice.model.Post;
import org.fastlink.mediaservice.model.Tag;
import org.fastlink.mediaservice.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@Service
@AllArgsConstructor
public class PostService
{

    private final TagHelper tagHelper;
    private final PostRepository postRepository;


    public List<Post> getAllPosts()
    {
        return postRepository.findAll();
    }

    public void savePost(Post post)
    {
        List<Tag> tagList = tagHelper.identifyTagsFromStringAndMap(post.getContent());

        if (tagList == null)
        {
            throw new ResponseStatusException(NOT_ACCEPTABLE, "nonexistent tag list ...");
        }

        tagHelper.checkForTagsAndAddIfnotFound(post.getTagList());
        post.setTagList(tagList);

        postRepository.save(post);
    }

    public Set<Post> getAllMediaForUser(Long userId)
    {
        return postRepository.findPostsByUserId(userId);
    }

    public Post findPostById(Long postId)
    {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));
    }

    public Long countPostsByUserId(Long userId) {
        return postRepository.countPostsByUserId(userId);
    }
}
