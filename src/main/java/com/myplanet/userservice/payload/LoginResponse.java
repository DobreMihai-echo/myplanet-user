package com.myplanet.userservice.payload;

import com.myplanet.userservice.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private UserDetails users;
    private String jwtToken;
    private String type;
}
