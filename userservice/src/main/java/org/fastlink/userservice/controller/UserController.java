package org.fastlink.userservice.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.userservice.dto.MediaUploadResponse;
import org.fastlink.userservice.dto.UserSearchedDto;
import org.fastlink.userservice.mapper.UserMapper;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.model.RegistrationVerificationToken;
import org.fastlink.userservice.model.Subscription;
import org.fastlink.userservice.request.GoogleRegistrationRequest;
import org.fastlink.userservice.request.SubscriptionRequest;
import org.fastlink.userservice.request.UserRegistrationRequest;
import org.fastlink.userservice.service.*;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@AllArgsConstructor
public class UserController
{
    private final SubscriptionService subscriptionService;

    private final FastlinkUserService fastlinkUserService;

    private final GoogleService googleService;

    private final TokenService tokenService;

   // private final JwtTokenUtil jwtUtil;

    private final FastlinkRoleService roleService;
    private final RestTemplate restTemplate;




    //	@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request)
            throws MessagingException, UnsupportedEncodingException
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/register").toUriString());
        return ResponseEntity.created(uri).body(fastlinkUserService.registerUser(request));
    }

    @PostMapping("/register/google")
    public ResponseEntity<FastlinkUser> registerGoogleUser(@RequestBody GoogleRegistrationRequest request)
            throws MessagingException, UnsupportedEncodingException
    {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath().path("/api/v1/users/register/google").toUriString()
        );

        return ResponseEntity.ok(googleService.registerUserFromGoogle(request));
    }

    @GetMapping("/exist/{userId}")
    public Boolean userExist(@PathVariable Long userId) {
        log.info("checking for user existence by id: {}", userId);
        return fastlinkUserService.userExistById(userId);
    }

    @PostMapping("/subscribe/{friendId}")
    public ResponseEntity<?> addUserSubscription(@RequestBody SubscriptionRequest subscriptionRequest, @PathVariable Long friendId)
    {
        //todo: get current user id from token
/*        Long subscriberId = jwtUtil.getUserIdFromJwt(jwtUtil.getTokenFromHeader(request.getHeader(AUTHORIZATION)));
        if (subscriberId == null) {
            log.error("error fetching current user Id");
            throw new ResponseStatusException(NOT_FOUND);
        }*/

        try
        {
            subscriptionService.addSubscription(subscriptionRequest.getSubscriberId(), friendId);
        } catch (Exception e) {
            log.error("couldn't save subscription ...");
        }

        return ResponseEntity.ok("subscription saved ...");
    }



    @GetMapping
    public List<FastlinkUser> getAllUsers()
    {
        return fastlinkUserService.getAllUsers();
    }

    @GetMapping("/subscriptions/{userId}")
    public List<FastlinkUser> getSubscriptions(@PathVariable Long userId)
    {
        List<Subscription> subscriptions = subscriptionService.getSubscriptions(userId);

        List<FastlinkUser> users = new ArrayList<>();

        subscriptions.forEach(subscription -> {
            users.add(subscription.getSubscribedTo());
        });

        return users;
    }

    @GetMapping("/subscribed/{userId}")
    public List<FastlinkUser> getSubscribed(@PathVariable Long userId)
    {
        List<Subscription> subscriptions = subscriptionService.getSubscribers(userId);

        List<FastlinkUser> users = new ArrayList<>();

        subscriptions.forEach(subscription -> {
            users.add(subscription.getSubscriber());
        });

        return users;
    }

    @RequestMapping(value = "/activate-account", method = {GET, PUT, POST})
    public ResponseEntity<String> activateUserAccount(@RequestParam("token") String verificationToken)
    {
        RegistrationVerificationToken token = tokenService.findToken(verificationToken);

        if (token == null)
        {
            log.error("Verification token {} not found", verificationToken);
            throw new ResponseStatusException(NOT_FOUND, "token not found ...");
        }

        FastlinkUser user = fastlinkUserService.findUserByEmailIgnoreCase(token.getUser().getEmail())
                .orElseThrow( () -> new ResponseStatusException( NOT_FOUND,"User not found ...") );

        user.setActive(true);
        roleService.addRoleToUser(user, "ROLE_USER");
        fastlinkUserService.saveUser(user);
        tokenService.deleteToken(token);

        return ResponseEntity.ok("Account activated successfully ...");
    }

    @GetMapping("/{username}")
    public ResponseEntity<FastlinkUser> getUserByUsername(@PathVariable("username") String username)
    {
        FastlinkUser user = fastlinkUserService.getUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found ..."));

        return ResponseEntity.ok(user);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<FastlinkUser> getUserById(@PathVariable("id") Long id)
    {
        FastlinkUser user = fastlinkUserService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found ..."));

        return ResponseEntity.ok(user);
    }
    @GetMapping("/contains/{username}")
    public ResponseEntity<List<UserSearchedDto>> getUsersLikeUsername(@PathVariable("username") String username) {
        List<FastlinkUser> users = fastlinkUserService.searchUserContaining(username);
        List<UserSearchedDto> userSearchedDtos = UserMapper.convertListEntityToDto(users)
                .stream()
                .peek(userSearchedDto -> userSearchedDto.setNumberOfSubscriber(fastlinkUserService.getUserSubscriber(userSearchedDto.getId())))
                .peek(userSearchedDto -> userSearchedDto.setNumberOfSubscribed_to(fastlinkUserService.getNumberOfSubscribed_to(userSearchedDto.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userSearchedDtos);
    }

    @GetMapping("/email/{email}")
    public FastlinkUser getUserByEmail(@PathVariable("email") String email)
    {
        log.info("getting user by email: {}", email);

        return fastlinkUserService.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found ..."));

        //return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/profile-picture/upload/{userId}", method = {POST, PUT})
    public ResponseEntity<String> uploadProfilePicture(@PathVariable("userId") Long userId, @RequestParam(name = "file") MultipartFile file) {
        if (file == null) {
            log.error("profile picture is null");
            return ResponseEntity.badRequest().body("profile picture is null");
        }

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", file.getResource());
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<MediaUploadResponse> response;
        try {
            response = restTemplate.exchange("http://localhost:9090/api/v1/file/upload?userId={userID}", HttpMethod.POST, requestEntity, MediaUploadResponse.class, userId);
        } catch (Exception e) {
            throw new RuntimeException("unable to upload file");
        }

        MediaUploadResponse responseBody = response.getBody();
        fastlinkUserService.saveProfilePictureToUser(userId, responseBody.getFileDownloadUri());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile-picture/{userId}")
    public ResponseEntity<String> getProfilePicture(@PathVariable("userId") Long userId) {
        String profilePicture = fastlinkUserService.findProfilePictureUrlByUserId(userId);
        return ResponseEntity.ok(profilePicture);
    }
}
