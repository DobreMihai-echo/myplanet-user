package com.myplanet.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    public Users(Users users) {
        this.firstName = users.getFirstName();
        this.lastName = users.getLastName();
        this.email = users.getEmail();
        this.username = users.getUsername();
        this.password = users.getPassword();
        this.enabled = users.getEnabled();
        this.locked = users.getLocked();
        this.roles = users.getRoles();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "organization_name")
    private String organizationName;

    private String email;

    private String username;

    private String password;

    private String phone;

    private Boolean enabled = false;

    private Boolean locked = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    @Column(name = "profile_picture")
    private String profilePhoto;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<TreePlantingActivity> treePlantingActivities;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<OrganizationJoiningActivity> organizationJoiningActivities;

    private Integer followerCount;
    private Integer followingCount;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "follow_users",
            joinColumns = @JoinColumn(name = "followed_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<Users> followerUsers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "join_users",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "joiner_id")
    )
    private List<Users> joiners = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "followerUsers")
    private List<Users> followingUsers = new ArrayList<>();

    @Column(name = "points")
    private Double points;

    private Boolean isOrganization;

    private String country;


}
