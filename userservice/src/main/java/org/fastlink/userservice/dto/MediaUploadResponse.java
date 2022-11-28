package org.fastlink.userservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaUploadResponse implements Serializable
{
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private String mediaCode;
    private String mediaType;
    private long size;
}