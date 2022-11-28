package org.fastlink.mediaservice.mapper;


import lombok.AllArgsConstructor;
import org.fastlink.mediaservice.dto.TagDto;
import org.fastlink.mediaservice.model.Tag;
import org.fastlink.mediaservice.repository.TagRepository;
import org.fastlink.mediaservice.services.TagService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TagMapper
{

    private final TagService tagService;

    public static TagDto convertEntityToDto(Tag tag)
    {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setHashtag(tag.getHashtag());
        return dto;
    }

    public static Tag convertDtoToEntity(TagDto dto)
    {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setHashtag(dto.getHashtag());
        return tag;
    }

    public Tag mapStringToTag(String string)
    {
        Tag tag;
        if (tagService.tagExistsByHashtag(string))
        {
            tag = tagService.findTagByHashtag(string);
        } else
        {
            tag = new Tag();
            tag.setHashtag(string);
        }

        return tag;
    }

}
