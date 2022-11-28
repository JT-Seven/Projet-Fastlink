package org.fastlink.mediastorageservice.res;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadMediaResponse implements Serializable
{
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private String mediaCode;
    private String mediaType;
    private long size;
}
