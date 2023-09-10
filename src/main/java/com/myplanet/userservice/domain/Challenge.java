package com.myplanet.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "challenge")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Users creator;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;

    @Column(name = "level")
    private String level;

    @Column(name = "points")
    private Long points;

    @Column(name = "color")
    private String color;

    @ManyToMany
    @JoinTable(
            name = "challenge_joiners",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> challengeJoiners = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "completed_joiners",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> completedJoiners = new HashSet<>(); // Assuming User entity is defined

    @ElementCollection
    @CollectionTable(name = "tag1", joinColumns = @JoinColumn(name = "challenge_id"))
    private List<Tag> challengeTags = new ArrayList<>(); // Assuming Tag entity is defined
}