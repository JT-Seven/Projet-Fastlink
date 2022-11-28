package org.fastlink.mediaservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class TagDto implements Serializable
{
    private Long id;
    private String hashtag;
}
