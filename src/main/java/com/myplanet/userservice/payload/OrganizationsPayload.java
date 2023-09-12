package com.myplanet.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationsPayload {
    private String name;
    private String email;
    private String phone;
    private int joiners;
}
