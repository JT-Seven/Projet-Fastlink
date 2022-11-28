package org.fastlink.searchservice.services;

import lombok.extern.slf4j.Slf4j;
import org.fastlink.searchservice.dto.UserDto;
import org.fastlink.searchservice.helper.SearchServiceRequestHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.fastlink.searchservice.helper.SearchServiceRequestHelper.USER_REQUEST_URL_CONTAINS_USERNAME;

@Slf4j
@Service
public class SearchService {
    private final RestTemplate restTemplate;

    public SearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public UserDto[] search(String username) {
        log.info("Searching for user with username: {}", username);
        UserDto[] user = restTemplate.getForObject(USER_REQUEST_URL_CONTAINS_USERNAME, UserDto[].class, username);
        log.info("Found user with username: {}", username);
        return user;
    }
    public Integer getMediaNumber(Long userId) {
        log.info("Searching for media number of user with id: {}", userId);
        Integer mediaNumber = restTemplate.getForObject(SearchServiceRequestHelper.MEDIA_NUMBER_REQUEST_URL_FROM_USER, Integer.class, userId);
        log.info("Found media number of user with id: {}", userId);
        return mediaNumber;
    }
}
