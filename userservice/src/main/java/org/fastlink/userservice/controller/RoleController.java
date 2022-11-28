package org.fastlink.userservice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.fastlink.userservice.model.FastlinkRole;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.service.FastlinkRoleService;
import org.fastlink.userservice.service.FastlinkUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController
{
    private final FastlinkRoleService roleService;

    private final FastlinkUserService userService;

    @GetMapping
    public ResponseEntity<List<FastlinkRole>> getAllRoles()
    {
        return ResponseEntity.ok().body(roleService.getAllRoles());
    }

    @PostMapping
    public ResponseEntity<FastlinkRole> saveRole(@RequestBody FastlinkRole role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/roles").toUriString());
        return ResponseEntity.created(uri).body(roleService.saveRole(role));
    }

    @PutMapping ("/to-user")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {

        FastlinkUser user = userService.getUserByUsername(form.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found ...")); // throws exc if not found

        roleService.addRoleToUser(user, form.getRolename());
        return ResponseEntity.ok().build();
    }
}

@Data @Getter
class RoleToUserForm {
	private String username;
	private String rolename;
}