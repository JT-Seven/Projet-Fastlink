package org.fastlink.mediaservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService
{

    @Autowired
    private RestTemplate restTemplate;

    public Boolean isAccountValid(Long userId)
    {

        return restTemplate.getForObject("http://localhost:8084/userExist/" + userId, Boolean.TYPE);

    }
}
