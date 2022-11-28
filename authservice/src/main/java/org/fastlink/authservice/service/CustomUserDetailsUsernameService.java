package org.fastlink.authservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.authservice.dto.PrivilegeDto;
import org.fastlink.authservice.dto.RoleDto;
import org.fastlink.authservice.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.fastlink.authservice.helper.UserServiceRequestHelper.USERLB_REQUEST_URL_BY_USERNAME;

@AllArgsConstructor
@Component
//@Qualifier("customUsername")
@Slf4j
public class CustomUserDetailsUsernameService implements UserDetailsService
{
    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        log.info("Enter loadUserByUsername - CustomUserDetailsService");
        log.info("username: {}", username);

        UserDto userDto;
        try
        {
            userDto = restTemplate.getForObject(USERLB_REQUEST_URL_BY_USERNAME, UserDto.class, username);
        } catch(Exception e)
        {
            log.error("Unable to fetch user");
            userDto = null;

            //throw new UsernameNotFoundException("Username or password incorrect");
        }

        //assert userDto != null;
        log.info("User fetched - {}", userDto.getUsername());
        log.info("user Id - {}", userDto.getId());

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

