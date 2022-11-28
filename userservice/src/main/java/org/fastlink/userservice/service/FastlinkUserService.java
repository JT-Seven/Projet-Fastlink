package org.fastlink.userservice.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.userservice.mapper.UserMapper;
import org.fastlink.userservice.model.FastlinkRole;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.model.RegistrationVerificationToken;
import org.fastlink.userservice.repository.FUserRepository;
import org.fastlink.userservice.repository.SubscriptionRepository;
import org.fastlink.userservice.request.UserRegistrationRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fastlink.userservice.model.Provider.LOCAL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class FastlinkUserService {
    private final SubscriptionRepository subscriptionRepository;
    private final FUserRepository userRepo;
    private final TokenService tokenService;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final FastlinkRoleService roleService;


    public Optional<FastlinkUser> findById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<FastlinkUser> getUserByUsername(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    public Map<String, ?> registerUser(UserRegistrationRequest request)
    {
        log.info("Saving new user {} to the db", request.getUsername());
        FastlinkUser fastlinkUser = UserMapper.mapRegistrationRequestToUser(request);

        FastlinkRole role = roleService.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Role ROLE_USER not found"));

        fastlinkUser.getRoles().add(role);

        Map<String, String> errorMap = new HashMap<>();
        Map<String, FastlinkUser> successMap = new HashMap<>();

        String valid =
                regexForm(
                        fastlinkUser.getUsername(),
                        fastlinkUser.getFirstName(),
                        fastlinkUser.getLastName(),
                        fastlinkUser.getPassword()
                );

        //todo: check field validitys

        if ( ! valid.equals("success") )
        {
            switch (valid)
            {
                case "firstname" ->
                {
                    errorMap.put("error", "firstname is invalid");
                    return errorMap;
                }
                case "lastname" ->
                {
                    errorMap.put("error", "lastname is invalid");
                    return errorMap;
                }
                case "username" ->
                {
                    errorMap.put("error", "username is invalid");
                    return errorMap;
                }
                case "password" ->
                {
                    errorMap.put("error", "password is invalid");
                    return errorMap;
                }
                default ->
                {
                }
            }
        }


        //todo: check if email valid
        Pattern pattern = Pattern.
                compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(fastlinkUser.getEmail());
        if (!matcher.matches()) throw new ResponseStatusException(BAD_REQUEST, "Invalid email address ...");


        //todo: check if email and username not taken
        if (
                userExistByUsername(fastlinkUser.getUsername()) || userExistByEmail(fastlinkUser.getEmail()) ||
                        userExistByUsername(fastlinkUser.getEmail()) || userExistByEmail(fastlinkUser.getUsername())
        ) {
            if (userExistByUsername(fastlinkUser.getUsername()) || userExistByUsername(fastlinkUser.getEmail())) {
                errorMap.put("error", "Username already taken");
            }
                //throw new ResponseStatusException(BAD_REQUEST, "username is taken ...");
            if (userExistByEmail(fastlinkUser.getEmail()) || userExistByEmail(fastlinkUser.getUsername())) {
                errorMap.put("error", "Email already taken");
            }
            return errorMap;
                //throw new ResponseStatusException(BAD_REQUEST, "email is taken ...");
        }


        fastlinkUser.setPassword(passwordEncoder.encode(fastlinkUser.getPassword()));
        fastlinkUser.setProvider(LOCAL.getProvider());
        userRepo.saveAndFlush(fastlinkUser);


        //Confirmation email handling
        RegistrationVerificationToken verificationToken = tokenService.save(new RegistrationVerificationToken(fastlinkUser));

        emailSenderService.sendEmail(emailSenderService.createConfirmationMail(fastlinkUser, verificationToken));

        successMap.put("success", fastlinkUser);

        return successMap;
    }

    public Long getIdFromDb(String username) {
        FastlinkUser fastlinkUser = getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found ...")); //if not found ( getUserByUsername ) throws exc

        return fastlinkUser.getId();
    }

    public Optional<FastlinkUser> findUserByEmailIgnoreCase(String email) {
        return userRepo.findByEmailIgnoreCase(email);
    }

    public boolean userExistById(Long userId) {
        return userRepo.existsById(userId);
    }

    public boolean userExistByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    public boolean userExistByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public List<FastlinkUser> getAllUsers() {
        return userRepo.findAll();
    }

    public void saveUser(FastlinkUser user) {
        userRepo.save(user);
    }

    private String regexForm(String username, String firstName, String lastName, String password) {
        {
            if (!username.matches("^[a-zA-Z0-9]*$")) {
                return "username";
            }
            if (!firstName.matches("^[a-zA-Z]*$")) {
                return "firstname";
            }
            if (!lastName.matches("^[a-zA-Z]*$")) {
                return "lastname";
            }

            if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                return "password";
            }

            return "success";
        }

    }

    public List<FastlinkUser> searchUserContaining(String search) {
        return userRepo.findByUsernameContaining(search);
    }

    public void saveProfilePictureToUser(Long userId, String pictureUrl) {
        FastlinkUser user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found ...")); //if not found ( getUserByUsername ) throws exc
        user.setProfilePictureUrl(pictureUrl);
        userRepo.save(user);
    }

    public String findProfilePictureUrlByUserId(Long userId) {
        FastlinkUser user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found ...")); //if not found ( getUserByUsername ) throws exc
        return user.getProfilePictureUrl();
    }

    public Integer getNumberOfSubscribed_to(Long userId) {
        return subscriptionRepository.countBySubscribedTo_Id(userId);
    }

    public  Integer getUserSubscriber(Long userId) {
        return subscriptionRepository.countBySubscriber_Id(userId);
    }
}
