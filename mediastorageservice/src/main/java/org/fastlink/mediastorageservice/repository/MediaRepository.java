package org.fastlink.mediastorageservice.repository;

import org.fastlink.mediastorageservice.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>
{
    Optional<Media> findByMediaCode(String mediaCode);
}

