package org.fastlink.mediastorageservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mediastorage")
@Getter
@Setter
public class MediaStorageConfig
{

    private String uploadDirectory;

}
