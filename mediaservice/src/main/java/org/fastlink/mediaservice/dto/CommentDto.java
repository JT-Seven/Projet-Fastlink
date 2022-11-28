package org.fastlink.mediaservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class CommentDto implements Serializable
{
    private Long commentId;
    private String description;
    private Long postId;
    private Long parentId;
    private int nbLike;
    private List<MediaDto> mediaList = new ArrayList<MediaDto>();
    private List<TagDto> tagList = new ArrayList<TagDto>();
    private String creationDate;
}
