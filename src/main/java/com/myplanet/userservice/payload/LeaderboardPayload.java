package com.myplanet.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardPayload {

    private String firstName;
    private String lastName;
    private byte[] profilePhoto;
    private Long points;
    private Long pointsMonth;
    private Long pointsYear;
}
