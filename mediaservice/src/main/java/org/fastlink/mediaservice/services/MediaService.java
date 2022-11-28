package org.fastlink.mediaservice.services;

import lombok.AllArgsConstructor;
import org.fastlink.mediaservice.dto.MediaDto;
import org.fastlink.mediaservice.mapper.MediaMapper;
import org.fastlink.mediaservice.model.Media;
import org.fastlink.mediaservice.repository.MediaRepository;
import org.fastlink.mediaservice.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MediaService {

    private final Path root = Paths.get("uploads");

    private final MediaRepository mediaRepository;

    private final PostRepository postRepository;


    private final MediaMapper mediaMapper;

    public List<MediaDto> getAllMedias() {
        return mediaRepository.findAll().stream().map(mediaMapper::convertEntityToDto).collect(Collectors.toList());
    }

    public void addMediaForAccount(Media media) {
        mediaRepository.save(media);
    }

/*    public List<MediaDto> getAllMediaForUser(Long userId)
    {

//        List<MediaDto> mediaDto = mediaRepository.findMediaByUserId(userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "media not found ..."));
        return new ArrayList<>(mediaRepository.findMediaByUserId(userId).filter(f -> f.get(a)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "")));
    }
    */


    public void deleteMedia(Long mediaId) {
        mediaRepository.deleteById(mediaId);
    }

    public Optional<Media> getMedia(Long mediaId) {
        return mediaRepository.findById(mediaId);
    }
public Integer getNumberMediasByUser(Long userId)
{
    return  mediaRepository.countMediaByUserId(userId);

}


}