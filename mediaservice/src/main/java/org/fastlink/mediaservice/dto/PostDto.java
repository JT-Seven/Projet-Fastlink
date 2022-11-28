package org.fastlink.mediaservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
public class PostDto implements Serializable
{
    private Long id;
    private Long userId;
    private String content;
    private int nbLike = 0;
    private int nbDislike = 0;
    private List<MediaDto> mediaList = new ArrayList<MediaDto>();
    private List<TagDto> tagList = new ArrayList<TagDto>();
    private List<CommentDto> commentList = new ArrayList<CommentDto>();
    private Date creationDate = new Date();
}
