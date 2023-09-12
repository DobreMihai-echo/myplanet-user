package com.myplanet.userservice.controller;

import com.myplanet.userservice.domain.*;
import com.myplanet.userservice.payload.LeaderboardPayload;
import com.myplanet.userservice.payload.RoleToUser;
import com.myplanet.userservice.payload.UserProfileRequest;
import com.myplanet.userservice.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UsersService service;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    @PostMapping("/user")
    public ResponseEntity<UsersBase> saveUser(@RequestBody Users usersBase){
        return ResponseEntity.ok().body(service.saveUser(usersBase));
    }

    @PostMapping("/role")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        return ResponseEntity.ok().body(service.saveRole(role));
    }

    @GetMapping("/user")
    public ResponseEntity<Users> getUser(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok().body(service.getUser(username));
    }

//    @PostMapping("/role/addToUser")
//    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUser roleToUser) {
//        service.addRoleToUer(roleToUser.getUsername(),roleToUser.getRoleName());
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/userid")
    public ResponseEntity<?> getUserById(@RequestParam(name = "userId") Long userId) {
        Users authUser = service.getAuthenticatedUser();
        Users targetUser = service.getUserById(userId);
        UserResponse userResponse = UserResponse.builder()
                .user(targetUser)
                .followedByAuthUser(targetUser.getFollowerUsers().contains(authUser))
                .build();
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/account/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") Long userId) {
        service.followUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/unfollow/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable("userId") Long userId) {
        service.unfollowUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/following")
    public ResponseEntity<?> getUserFollowingUsers(@PathVariable("userId") Long userId,
                                                   @RequestParam("page") Integer page,
                                                   @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UsersBase> followingList = service.getFollowingUsersPaginate(userId, page, size);
        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/follower")
    public ResponseEntity<?> getUserFollowerUsers(@PathVariable("userId") Long userId,
                                                  @RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UsersBase> followingList = service.getFollowerUsersPaginate(userId, page, size);
        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUser(@RequestParam("key") String key,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<UserResponse> userSearchResult = service.getUserSearchResult(key, page, size);
        return new ResponseEntity<>(userSearchResult, HttpStatus.OK);
    }

    @PostMapping(value = "/users/event", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllForEvent(@RequestBody List<String> usernames) {
        if (usernames.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(service.getAllUsersByUsername(usernames));
    }

    @PutMapping(value = "/user/points")
    public ResponseEntity<?> addPoints(@RequestBody Double pointsToAdd) {
        return ResponseEntity.ok(service.addPoints(pointsToAdd));
    }

    @GetMapping(value = "/organizations")
    public ResponseEntity<?> getOrganizations(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok(service.getOrganizationData(username));
    }

    @GetMapping(value = "/users/organizations/all")
    public ResponseEntity<?> getAllOrganizations() {
        return ResponseEntity.ok(service.getOrganizations());
    }

    @PutMapping(value = "/user/profile", consumes = "multipart/form-data")
    public ResponseEntity<?> editProfile(@RequestParam(name = "profilePicture")MultipartFile profile, @RequestParam(name = "coverPicture") MultipartFile cover,@RequestParam(name = "username") String username, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email,@RequestParam(name = "phone") String phone , @RequestParam(name = "about") String about) {
        try {
            return ResponseEntity.ok(service.updateUserProfile(profile,cover,username,firstName,lastName,email,phone,about));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("An error occured:" + ex.getMessage());
        }
    }

    @PutMapping(value = "/organization/join")
    public ResponseEntity<?> joinOrganization(@RequestParam String organizationName) {
        return ResponseEntity.ok(service.joinOrganization(organizationName));
    }

    @GetMapping(value = "/organization")
    public ResponseEntity<?> getJoinersByRegion(@RequestParam(name = "organizationID") Long organizationID, @RequestParam(name = "country") String country) {
        return ResponseEntity.ok(service.getJoinersByRegion(organizationID, country));
    }

    @GetMapping(value = "/organization/joiners")
    public ResponseEntity<?> getJoiners(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok(service.getJoiners(username));
    }

    @PutMapping(value = "/organization/addRole")
    public ResponseEntity<?> addRole(@RequestParam(name = "role") String role, @RequestParam(name = "username") String username) {
        return ResponseEntity.ok(service.addRole(role,username));
    }

    @PutMapping(value = "/user/plant-trees")
    public ResponseEntity<?> plantTrees(@RequestParam(name = "userID") Long userID, @RequestParam(name = "trees") Long trees) {
        return ResponseEntity.ok(service.plantTree(userID,trees));
    }

    @GetMapping(value = "/organization/chart")
    public ResponseEntity<?> chartData(@RequestParam(name = "country") String country, @RequestParam(name = "organizationID") Long organizationID) {
        return ResponseEntity.ok(service.getInformation(country,organizationID));
    }

    @GetMapping("/organization/leaderboard")
    public ResponseEntity<List<LeaderboardPayload>> getMembersSortedByPoints() {
        List<LeaderboardPayload> sortedMembers = service.getLeaderboardForOrganization();
        return new ResponseEntity<>(sortedMembers, HttpStatus.OK);
    }
}
