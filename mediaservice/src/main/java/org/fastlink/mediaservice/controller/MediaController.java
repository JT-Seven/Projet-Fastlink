package org.fastlink.mediaservice.controller;


import lombok.AllArgsConstructor;
import org.fastlink.mediaservice.dto.MediaDto;
import org.fastlink.mediaservice.mapper.MediaMapper;
import org.fastlink.mediaservice.model.Media;
import org.fastlink.mediaservice.services.MediaService;
import org.fastlink.mediaservice.services.PostService;
import org.fastlink.mediaservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medias")
@AllArgsConstructor
public class MediaController
{

    private final MediaService mediaService;

    private final UserService userService;

    private final PostService postService;

    private final HttpServletRequest request;

    private final MediaMapper mediaMapper;

    @GetMapping()
    public List<MediaDto> getAllMedias()
    {
        return mediaService.getAllMedias();
    }

    @GetMapping("/{mediaId}")
    public MediaDto getMedia(@PathVariable(value = "mediaId") Long mediaId)
    {
        Media media = mediaService.getMedia(mediaId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));

        return mediaMapper.convertEntityToDto(media);
    }


//TODO: media download
/*
    @GetMapping("/all")
    public List<MediaDto> getAllMediasByUser(@RequestParam(value = "userId") Long userId)
    {
        return mediaService.getAllMediasByUser(userId);
    }
*/
    /*
    @GetMapping("/search_profile/media/{userId}")
    public List<MediaDto> getAllMediasByUser(@PathVariable(value = "userId") Long userId)
    {
        return mediaService.getAllMediasByUser(userId);
    }

     */
@GetMapping("/search_profile/media_number/{userId}")

public ResponseEntity<Integer> getNumberMediasByUser( @PathVariable Long userId)
    {
        return ResponseEntity.ok(mediaService.getNumberMediasByUser(userId));


    }

    @PostMapping
    public ResponseEntity<String> addMediaForAccount(@RequestBody MediaDto mediaDto)
    {
        Media media = mediaMapper.convertDtoToEntity(mediaDto);
        mediaService.addMediaForAccount(media);
        return ResponseEntity.ok().body("Media added successfully ...");
    }

    @DeleteMapping("/delete/{mediaId}")
    public ResponseEntity<String> deleteMedia(@PathVariable(value = "mediaId") Long mediaId)
    {
        Media media = mediaService.getMedia(mediaId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));

        mediaService.deleteMedia(media.getMediaId());
        return ResponseEntity.ok().body("Media deleted successfully ...");
    }
}
