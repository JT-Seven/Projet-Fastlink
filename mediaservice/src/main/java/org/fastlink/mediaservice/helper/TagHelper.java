package org.fastlink.mediaservice.helper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.fastlink.mediaservice.mapper.TagMapper;
import org.fastlink.mediaservice.model.Tag;
import org.fastlink.mediaservice.repository.TagRepository;
import org.fastlink.mediaservice.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Setter
public class TagHelper
{


    private final TagMapper tagMapper;
    private final TagService tagService;

    public List<Tag> identifyTagsFromStringAndMap(String str)
    {
        List<String> stringList = new ArrayList<String>();
        Pattern MY_PATTERN = Pattern.compile("#(\\w+)");
        Matcher matcher = MY_PATTERN.matcher(str);

        while (matcher.find())
        {
            stringList.add(matcher.group(1));
        }

        return stringList
                .stream()
                .map(tagMapper::mapStringToTag)
                .collect(Collectors.toList());
    }

    public void checkForTagsAndAddIfnotFound(List<Tag> tagList)
    {
        for (Tag tag : tagList)
        {

            if (!tagService.tagExistsByHashtag(tag.getHashtag()))
            {
                tagService.saveTag(tag);
            }

        }
    }
}
