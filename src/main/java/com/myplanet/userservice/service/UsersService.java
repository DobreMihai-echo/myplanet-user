package com.myplanet.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myplanet.userservice.domain.*;
import com.myplanet.userservice.payload.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface UsersService {

    UsersBase registerUser(SignupRequest request) throws JsonProcessingException;

    Boolean existsByUsername(String username);

    String confirmToken(String token);


    Users saveUser(Users user);
    Role saveRole(Role role);

    Users getUser(String username);
    List<Users> getUsers();

    Users getUserById(Long id);

    Users updateUserProfile(MultipartFile profile, MultipartFile cover, String username, String firstName, String lastName, String email, String phone, String about) throws IOException;

    UsersBase getUserByUsername(String username);

    List<UsersBase> getFollowerUsersPaginate(Long userId, Integer page, Integer size);

    List<UsersBase> getFollowingUsersPaginate(Long userId, Integer page, Integer size);

    void followUser(Long userId);

    void unfollowUser(Long userId);

    List<UserResponse> getUserSearchResult(String key, Integer page, Integer size);

    UserResponse userToUserResponse(Users user);

    List<Users> getAllUsersByUsername(List<String> usernames);

    Users getAuthenticatedUser();

    UsersBase addPoints(Double points);

    Collection<? extends GrantedAuthority> joinOrganization(String organizationName);

    UsersBase addRole(String roleToAdd, String username);

    List<Users> getJoinersByRegion(Long organizationId, String country);

    List<JoinerResponse> getJoiners(String username);

    UsersBase plantTree(Long userId, Long trees);

    CountryChartDataResponse getInformation(String country, Long organizationID);

    UsersBase changePermission(String roleName);

    OrganizationDTO getOrganizationData(String username);

    List<OrganizationsPayload> getOrganizations();

    List<LeaderboardPayload> getLeaderboardForOrganization();

}
