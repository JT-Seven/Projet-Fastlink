package org.fastlink.mediaservice.repository;


import org.fastlink.mediaservice.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>
{
    Optional<Tag> findTagByHashtag(String hashtag);
    boolean existsByHashtag(String hashtag);
}