package org.fastlink.mediastorageservice.service;

import lombok.AllArgsConstructor;
import org.fastlink.mediastorageservice.model.Media;
import org.fastlink.mediastorageservice.repository.MediaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service @AllArgsConstructor
public class MediaService
{
    private final MediaRepository mediaRepository;

    public Media findByMediaCode(String mediaCode)
    {
        return mediaRepository.findByMediaCode(mediaCode)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Media not found ..."));
    }
}
