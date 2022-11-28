package org.fastlink.authservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.authservice.dto.PrivilegeDto;
import org.fastlink.authservice.dto.RoleDto;
import org.fastlink.authservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.fastlink.authservice.helper.UserServiceRequestHelper.USER_REQUEST_URL_BY_EMAIL;

@AllArgsConstructor
@Component
//@Qualifier("customEmail")
@Slf4j
public class CustomUserDetailsEmailService implements UserDetailsService
{

    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {

        log.info("Enter loadUserByUsername - CustomUserDetailsService");
        log.info("email: {}", username);

        UserDto userDto;
        try
        {
            userDto = restTemplate.getForObject(USER_REQUEST_URL_BY_EMAIL, UserDto.class, username);
        } catch(Exception e)
        {
            throw new RuntimeException("unable to fetch user");
        }


        log.info("User fetched - {}", userDto.getUsername());
        log.info("user Id - {}", userDto.getId());

        for (GrantedAuthority a : getGrantedAuthorities(userDto.getRoles()))
        {
            log.info("authority: {}", a.getAuthority());
        }

        return new org.springframework.security.core.userdetails.User(
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.isActive(),
                true,
                true,
                true,
                getGrantedAuthorities(userDto.getRoles())
        );
    }


    private List<GrantedAuthority> getGrantedAuthorities(List<RoleDto> roles)
    {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (RoleDto role : roles)
        {
            for (PrivilegeDto privilege : role.getPrivileges())
            {
                if ( ! authorities.contains(new SimpleGrantedAuthority(privilege.getName())))
                {
                    authorities.add(new SimpleGrantedAuthority(privilege.getName()));
                }
            }

            if ( ! authorities.contains(new SimpleGrantedAuthority(role.getName())) )
            {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        }

        return authorities;
    }
}
