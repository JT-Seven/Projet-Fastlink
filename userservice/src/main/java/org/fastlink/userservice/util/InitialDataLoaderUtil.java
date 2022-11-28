/*
package org.fastlink.userservice.util;

import org.fastlink.userservice.model.FastlinkPrivilege;
import org.fastlink.userservice.model.FastlinkRole;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.model.Provider;
import org.fastlink.userservice.repository.FPrivilegeRepository;
import org.fastlink.userservice.repository.FRoleRepository;
import org.fastlink.userservice.repository.FUserRepository;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class InitialDataLoaderUtil implements ApplicationListener<ContextRefreshedEvent>
{

    boolean alreadySetup;
    private final PasswordEncoder encoder;
    private final FUserRepository fUserRepository;
    private final FRoleRepository FRoleRepository;
    private final FPrivilegeRepository FPrivilegeRepository;

    public InitialDataLoaderUtil(
            PasswordEncoder encoder,
            FUserRepository fUserRepository,
            org.fastlink.userservice.repository.FRoleRepository fRoleRepository,
            org.fastlink.userservice.repository.FPrivilegeRepository fPrivilegeRepository)
    {
        this.encoder = encoder;
        this.fUserRepository = fUserRepository;
        this.FRoleRepository = fRoleRepository;
        this.FPrivilegeRepository = fPrivilegeRepository;
        this.alreadySetup = false;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if (alreadySetup) return;

        FRoleRepository.save(new FastlinkRole("ROLE_ADMIN"));
        FRoleRepository.save(new FastlinkRole("ROLE_USER"));

        FastlinkRole adminRole = FRoleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException(""));
        FastlinkRole userRole = FRoleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException(""));

        FastlinkPrivilege userReadPrivilege = createPrivilegeIfNotFound("user:read");
        FastlinkPrivilege userWritePrivilege = createPrivilegeIfNotFound("user:write");
        FastlinkPrivilege mediaReadPrivilege = createPrivilegeIfNotFound("media:read");
        FastlinkPrivilege mediaWritePrivilege = createPrivilegeIfNotFound("media:write");

        List<FastlinkPrivilege> adminPrivileges = Arrays.asList(userReadPrivilege, userWritePrivilege, mediaWritePrivilege, mediaReadPrivilege);
        List<FastlinkPrivilege> userPrivileges = Arrays.asList(userReadPrivilege, mediaReadPrivilege, mediaWritePrivilege, userWritePrivilege);

        adminRole.setPrivileges(adminPrivileges);
        userRole.setPrivileges(userPrivileges);

        FastlinkUser adminUser = new FastlinkUser();
        adminUser.setFirstName("Sandro");
        adminUser.setLastName("Bakuradze");
        adminUser.setUsername("SandroB");
        adminUser.setEmail("bakuradze@et.intechinfo.fr");
        adminUser.setPassword(encoder.encode("sandrika123"));
        adminUser.setProvider(Provider.LOCAL.getProvider());
        adminUser.setRoles(Arrays.asList(adminRole, userRole));

        FastlinkUser regularUser = new FastlinkUser();
        regularUser.setFirstName("Mehdi");
        regularUser.setLastName("Teranee");
        regularUser.setUsername("mtrn");
        regularUser.setEmail("mehdi@gmail.com");
        regularUser.setPassword(encoder.encode("password"));
        regularUser.setRoles(List.of(userRole));
        regularUser.setProvider(Provider.LOCAL.getProvider());

        fUserRepository.save(adminUser);
        fUserRepository.save(regularUser);

        alreadySetup = true;
    }

    @Transactional
    FastlinkRole createRoleIfNotFound(String name, List<FastlinkPrivilege> privileges)
    {
        Optional<FastlinkRole> roleToFind = FRoleRepository.findByName(name);
        if (roleToFind.isPresent())
        {
            return roleToFind.get();
        }
        FastlinkRole role = new FastlinkRole(name);
        role.setPrivileges(privileges);
        return FRoleRepository.save(role);
    }

    @Transactional
    FastlinkPrivilege createPrivilegeIfNotFound(String name)
    {
        FastlinkPrivilege privilege = FPrivilegeRepository.findByName(name);
        if (privilege == null)
        {
            privilege = new FastlinkPrivilege(name);
            FPrivilegeRepository.save(privilege);
        }
        return privilege;
    }
}

*/
