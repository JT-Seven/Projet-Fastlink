package org.fastlink.userservice.repository;

import org.fastlink.userservice.model.FastlinkRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository
public interface FRoleRepository extends JpaRepository<FastlinkRole, Long>
{
    Optional<FastlinkRole> findByName(String name) throws ResponseStatusException;
}