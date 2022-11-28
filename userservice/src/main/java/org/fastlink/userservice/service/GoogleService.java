package org.fastlink.userservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.userservice.mapper.UserMapper;
import org.fastlink.userservice.model.FastlinkRole;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.request.GoogleRegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.fastlink.userservice.model.Provider.GOOGLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class GoogleService
{
    private final FastlinkUserService  userService;
    private final FastlinkRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;


    public FastlinkUser registerUserFromGoogle(GoogleRegistrationRequest request)
            throws ResponseStatusException, MessagingException, UnsupportedEncodingException
    {
        FastlinkUser fastlinkUser = UserMapper.mapGoogleRegistrationRequestToUser(request);

        if (userService.userExistByEmail(fastlinkUser.getEmail()))
        {
            log.info("UserFromGoogle {} already exists in the db", fastlinkUser.getEmail());

            return userService.findUserByEmailIgnoreCase(fastlinkUser.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found ..."));
        }

        FastlinkRole role = roleService.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Role not found ..."));

        fastlinkUser.getRoles().add(role);
        fastlinkUser.setProvider(GOOGLE.getProvider());
        fastlinkUser.setActive(true);
        fastlinkUser.setDescription("");


        fastlinkUser.setPassword(passwordEncoder.encode(""));


        userService.saveUser(fastlinkUser);
        log.info("UserFromGoogle {} saved to the db", fastlinkUser.getUsername());

        emailSenderService.sendEmail(emailSenderService.createPasswordNotificationMail(fastlinkUser, "randomizedCharacter"));

        return fastlinkUser;
    }
}
