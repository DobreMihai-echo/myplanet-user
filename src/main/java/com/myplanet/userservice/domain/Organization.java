package com.myplanet.userservice.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organization")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Organization extends UsersBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "organization_name")
    private String organizationName;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<TreePlantingActivity> treePlantingActivities;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<OrganizationJoiningActivity> organizationJoiningActivities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "join_users",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "joiner_id")
    )
    private List<Users> joiners = new ArrayList<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrganizationMemberPoints> organizationMembers = new ArrayList<>();
}
