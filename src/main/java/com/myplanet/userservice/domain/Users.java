package com.myplanet.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Users extends UsersBase{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Type(type = "org.hibernate.type.BinaryType")
    @Lob
    private byte[] profilePhoto;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] coverPhoto;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<TreePlantingActivity> treePlantingActivities;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<OrganizationJoiningActivity> organizationJoiningActivities;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<CarbonFootprint> carbonFootprints;

    private String about;



    private Integer followerCount;
    private Integer followingCount;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "follow_users",
            joinColumns = @JoinColumn(name = "followed_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<Users> followerUsers = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followerUsers")
    private List<Users> followingUsers = new ArrayList<>();

    @Column(name = "points")
    private Double points;

    private String country;

}
