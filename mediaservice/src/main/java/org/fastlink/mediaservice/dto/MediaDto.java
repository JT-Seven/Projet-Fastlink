package org.fastlink.mediaservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.fastlink.mediaservice.model.MediaResourceType;
import org.fastlink.mediaservice.model.MediaType;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MediaDto implements Serializable
{

    private Long mediaId;
    private Long userId;
    private MediaType mediaType;
    private MediaResourceType mediaResourceType;
    private Date createdAt;
    private Long postId;
    private String mediaCode;
    private String downloadUrl;
    private String mimeType;
    private String fileName;
/*
    private MediaType mediaType;
    private Blob image;
    private String url;
*/

}
