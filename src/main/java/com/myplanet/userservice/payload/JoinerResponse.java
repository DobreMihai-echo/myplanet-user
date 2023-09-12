package com.myplanet.userservice.payload;

import com.myplanet.userservice.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinerResponse {

    private String username;
    private byte[] profilePicture;
    private String name;
    private String email;
    private String phone;
    private LocalDate date;
    private String country;
    private List<Role> roles;
}
