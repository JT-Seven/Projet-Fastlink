package org.fastlink.mediaservice.services;

import lombok.AllArgsConstructor;
import org.fastlink.mediaservice.model.Tag;
import org.fastlink.mediaservice.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService
{
    private final TagRepository tagRepository;

    public Tag findTagByHashtag(String hashtag)
    {
        return tagRepository.findTagByHashtag(hashtag)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
    }

    public List<Tag> getAllTags()
    {
        return tagRepository.findAll();
    }

    public boolean tagExistsByHashtag(String hashtag)
    {
        return tagRepository.existsByHashtag(hashtag);
    }

    public void saveTag(Tag tag)
    {
        tagRepository.save(tag);
    }
}
