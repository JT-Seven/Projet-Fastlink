package org.fastlink.mediaservice.repository;

import org.fastlink.mediaservice.dto.MediaDto;
import org.fastlink.mediaservice.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>
{

    Optional<List<MediaDto>> findMediaByUserId(Long userId);
    Integer countMediaByUserId(Long userId);

}
