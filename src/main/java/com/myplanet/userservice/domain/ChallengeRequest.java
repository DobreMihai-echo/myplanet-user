package com.myplanet.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeRequest {

    private String title;

    private String description;

    private Users creator;

    private Organization organization;

    private String level;

    private Long points;

    private String color;

    private List<Tag> challengeTags = new ArrayList<>();
}
