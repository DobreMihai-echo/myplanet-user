package com.myplanet.userservice.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UsersBase user;
    private Boolean followedByAuthUser;
}
