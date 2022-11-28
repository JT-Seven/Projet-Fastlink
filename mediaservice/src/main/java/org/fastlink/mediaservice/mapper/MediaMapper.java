package org.fastlink.mediaservice.mapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.mediaservice.dto.MediaDto;
import org.fastlink.mediaservice.model.Media;
import org.fastlink.mediaservice.services.MediaService;
import org.fastlink.mediaservice.services.PostService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class MediaMapper
{

    private final PostService postService;
    public Media convertDtoToEntity(MediaDto mediaDto)
    {
        Media media = new Media();
        media.setMediaId(mediaDto.getMediaId());
        media.setCreatedAt(mediaDto.getCreatedAt());
        media.setMediaResourceType(mediaDto.getMediaResourceType());
        media.setUserId(mediaDto.getUserId());
        media.setMediaType(mediaDto.getMediaType());
        media.setPost(postService.findPostById(mediaDto.getPostId()));
        media.setMediaCode(mediaDto.getMediaCode());
        media.setDownloadUrl(mediaDto.getDownloadUrl());
        media.setMimeType(mediaDto.getMimeType());
        media.setFileName(mediaDto.getFileName());
        return media;
    }

    public MediaDto convertEntityToDto(Media media)
    {
        MediaDto mediaDto = new MediaDto();
        mediaDto.setUserId(media.getUserId());
        mediaDto.setMediaId(media.getMediaId());
        mediaDto.setMediaResourceType(media.getMediaResourceType());
        mediaDto.setMediaType(media.getMediaType());
        mediaDto.setCreatedAt(media.getCreatedAt());
        mediaDto.setMediaResourceType(media.getMediaResourceType());
        mediaDto.setMediaCode(media.getMediaCode());
        mediaDto.setDownloadUrl(media.getDownloadUrl());
        mediaDto.setPostId(media.getPost().getId());
        mediaDto.setMimeType(media.getMimeType());
        mediaDto.setFileName(mediaDto.getFileName());
        return mediaDto;
    }


}
