package com.myplanet.userservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ROLE_ORGANIZATION_READ("organization:read"),
    ROLE_ORGANIZATION_UPDATE("organization:update"),
    ROLE_ORGANIZATION_CREATE("organization:create"),
    ROLE_ORGANIZATION_DELETE("organization:delete");


    @Getter
    private final String permission;
}
