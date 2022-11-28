package org.fastlink.mediaservice.repository;

import lombok.NonNull;
import org.fastlink.mediaservice.dto.PostDto;
import org.fastlink.mediaservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long>
{
    Set<Post> findPostsByUserId(Long userId);

    Optional<Post> findById(Long id);

    Long countPostsByUserId(Long userId);
}