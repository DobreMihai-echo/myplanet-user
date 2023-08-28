package com.myplanet.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myplanet.userservice.domain.Role;
import com.myplanet.userservice.domain.TreePlantingActivity;
import com.myplanet.userservice.domain.UserResponse;
import com.myplanet.userservice.domain.Users;
import com.myplanet.userservice.payload.CountryChartDataResponse;
import com.myplanet.userservice.payload.SignupRequest;
import org.apache.catalina.User;

import java.util.List;

public interface UsersService {

    Users registerUser(SignupRequest request) throws JsonProcessingException;

    String confirmToken(String token);


    Users saveUser(Users user);
    Role saveRole(Role role);

    void addRoleToUer(String username, String roleName);
    Users getUser(String username);
    List<Users> getUsers();

    Users getUserById(Long id);

    Users getUserByUsername(String username);

    List<Users> getFollowerUsersPaginate(Long userId, Integer page, Integer size);

    List<Users> getFollowingUsersPaginate(Long userId, Integer page, Integer size);

    void followUser(Long userId);

    void unfollowUser(Long userId);

    List<UserResponse> getUserSearchResult(String key, Integer page, Integer size);

    UserResponse userToUserResponse(Users user);

    List<Users> getAllUsersByUsername(List<String> usernames);

    Users getAuthenticatedUser();

    Users addPoints(Double points);

    void joinOrganization(Long organizationId);

    List<Users> getJoinersByRegion(Long organizationId, String country);

    Users plantTree(Long userId, Long trees);

    CountryChartDataResponse getInformation(String country, Long organizationID);

}
