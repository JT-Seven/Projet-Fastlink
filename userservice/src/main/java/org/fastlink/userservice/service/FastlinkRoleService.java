package org.fastlink.userservice.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.userservice.model.FastlinkRole;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.repository.FRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class FastlinkRoleService
{
    private final FRoleRepository fRoleRepository;

    @Transactional
    public void addRoleToUser(FastlinkUser user, String rolename) {
        log.info("Adding role {} to user {}", rolename, user.getUsername());

        FastlinkRole role = fRoleRepository.findByName(rolename)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Role not found ..." ));

        user.getRoles().add(role);
    }

    public FastlinkRole saveRole(FastlinkRole role) {
        log.info("Saving new role {} to the db", role.getName());
        return fRoleRepository.save(role);
    }

    public Optional<FastlinkRole> findRoleByName(String roleName)
    {
        return fRoleRepository.findByName(roleName);
    }

    public List<FastlinkRole> getAllRoles()
    {
        return fRoleRepository.findAll();
    }

}
