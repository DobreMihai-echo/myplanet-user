package com.myplanet.userservice.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Users user;
    private Boolean followedByAuthUser;
}
