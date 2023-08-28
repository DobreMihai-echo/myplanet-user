package com.myplanet.userservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum ERole {
    ROLE_USER(Collections.emptySet()),
    ROLE_ORGANIZATION(
            Set.of(Permission.ROLE_ORGANIZATION_READ,Permission.ROLE_ORGANIZATION_UPDATE,Permission.ROLE_ORGANIZATION_DELETE,Permission.ROLE_ORGANIZATION_CREATE)
    ),
    ROLE_ADMIN(Collections.emptySet());

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream().map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority(this.name()));
        return authorities;
    };

}

