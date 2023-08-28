package com.myplanet.userservice.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myplanet.userservice.domain.*;
import com.myplanet.userservice.payload.CountryChartDataResponse;
import com.myplanet.userservice.payload.SignupRequest;
import com.myplanet.userservice.payload.TreeDataResponse;
import com.myplanet.userservice.repository.RolesRepository;
import com.myplanet.userservice.repository.TreePlantingRepository;
import com.myplanet.userservice.repository.UsersRepository;
import com.myplanet.userservice.service.ConfirmationTokenService;
import com.myplanet.userservice.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsersServiceImpl implements UsersService {
    private final UsersRepository userRepository;
    private final RolesRepository rolesRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final TreePlantingRepository treePlantingRepository;

    private final PasswordEncoder encoder;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public Users registerUser(SignupRequest request) throws JsonProcessingException {
        boolean userExists = userRepository.findByUsername(request.getUsername()).isPresent();

        if (userExists) {
            throw new IllegalStateException("Username already taken");
        }
        List<Role> userRoles = new ArrayList<>();
        String encodedPassword = encoder.encode(request.getPassword());
        Users user;
        if (request.getOrganizationName()!=null) {
            if (!rolesRepository.existsByName(ERole.ROLE_ORGANIZATION)) {
                rolesRepository.save(new Role(ERole.ROLE_ORGANIZATION));
            }

            userRoles.add(new Role(ERole.ROLE_ORGANIZATION));
            user = Users.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(encodedPassword)
                    .organizationName(request.getOrganizationName())
                    .phone(request.getPhone())
                    .roles(userRoles)
                    .joiners(new ArrayList<>())
                    .isOrganization(true)
                    .build();
        } else {
            userRoles.add(new Role(ERole.ROLE_USER));
            user = Users.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(encodedPassword)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phone(request.getPhone())
                    .roles(userRoles)
                    .points(0D)
                    .followerCount(0)
                    .treePlantingActivities(new ArrayList<>())
                    .followerCount(0)
                    .followerUsers(new ArrayList<>())
                    .followingUsers(new ArrayList<>())
                    .country(request.getCountry())
                    .build();
        }

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .confirmedAt(null)
                .user(user)
                .build();

        confirmationTokenService.generateConfirmationToken(confirmationToken);

        String link = "http://localhost:8081/api/auth/confirm?token=" + token;

        Message message = Message.builder()
                .toEmail(user.getEmail())
                .username(user.getUsername())
                .toPhone(user.getPhone())
                .message("Welcome to MyPlanet Application")
                .subject("Activate account")
                .token(link)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(message);
        kafkaTemplate.send("myplanet_notification_topic", jsonString);
        return user;
    }

    @Override
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        enableAccount(confirmationToken.getUser().getUsername());

        return "confirmed";

    }

    private void enableAccount(String username) {
        Users users = userRepository.findByUsername(username).orElseThrow();
        users.setEnabled(true);
        userRepository.save(users);
    }

    @Override
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return rolesRepository.save(role);
    }

    @Override
    public void addRoleToUer(String username, String roleName) {

    }

    @Override
    public Users getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public List<Users> getFollowerUsersPaginate(Long userId, Integer page, Integer size) {
        Users targetUser = getUserById(userId);
        return new ArrayList<>(userRepository.findUsersByFollowingUsers(targetUser,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))));
    }

    @Override
    public List<Users> getFollowingUsersPaginate(Long userId, Integer page, Integer size) {
        Users targetUser = getUserById(userId);
        return new ArrayList<>(userRepository.findUsersByFollowerUsers(targetUser,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))));
    }

    @Override
    public void followUser(Long userId) {
        Users authUser = getAuthenticatedUser();
        if (!authUser.getId().equals(userId)) {
            Users userToFollow = getUserById(userId);
            authUser.getFollowingUsers().add(userToFollow);
            authUser.setFollowingCount(authUser.getFollowingCount() + 1);
            userToFollow.getFollowerUsers().add(authUser);
            userToFollow.setFollowerCount(userToFollow.getFollowerCount() + 1);
            userRepository.save(userToFollow);
            userRepository.save(authUser);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void unfollowUser(Long userId) {
        Users authUser = getAuthenticatedUser();
        if (!authUser.getId().equals(userId)) {
            Users userToUnfollow = getUserById(userId);
            authUser.getFollowingUsers().remove(userToUnfollow);
            authUser.setFollowingCount(authUser.getFollowingCount() - 1);
            userToUnfollow.getFollowerUsers().remove(authUser);
            userToUnfollow.setFollowerCount(userToUnfollow.getFollowerCount() - 1);
            userRepository.save(userToUnfollow);
            userRepository.save(authUser);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public List<UserResponse> getUserSearchResult(String key, Integer page, Integer size) {
        return userRepository.findUsersByUsername(
                key,
                PageRequest.of(page, size)
        ).stream().map(this::userToUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse userToUserResponse(Users user) {
        Users authUser = userRepository.findByUsername("test").get();
        return UserResponse.builder()
                .user(user)
                .followedByAuthUser(user.getFollowerUsers().contains(authUser))
                .build();
    }

    @Override
    public List<Users> getAllUsersByUsername(List<String> usernames) {
        return userRepository.findByUsernameIn(usernames);
    }

    public Users getAuthenticatedUser() {
        System.out.println("userRepository: " + userRepository);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String authUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
            Optional<Users> optionalUsers = userRepository.findByUsername(authUsername);
            if (optionalUsers.isPresent()) {
                return optionalUsers.get();
            } else {
                System.out.println("ELSE 1");
            }
        } else {
            System.out.println("ELSE 2");
        }
        System.out.println("WORSE");
        return null;
    }

    @Override
    public Users addPoints(Double points) {
        Users userToAddPoints = getAuthenticatedUser();
        System.out.println("AUTHENTICATED USER:::::" + userToAddPoints);
        userToAddPoints.setPoints(userToAddPoints.getPoints() + points);

        return userRepository.save(userToAddPoints);
    }

    @Override
    public void joinOrganization(Long organizationId) {
        Users authUser = getAuthenticatedUser();
        if (!authUser.getId().equals(organizationId)) {
            Users organizationToJoin = getUserById(organizationId);
            organizationToJoin.getJoiners().add(authUser);
            organizationToJoin.getOrganizationJoiningActivities().add(OrganizationJoiningActivity
                    .builder()
                            .organization(organizationToJoin)
                            .users(authUser)
                            .date(LocalDate.now())
                    .build());
            userRepository.save(organizationToJoin);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Users> getJoinersByRegion(Long organizationId, String country) {
        return userRepository.findJoinersByOrganizationAndCountry(organizationId,country);
    }

    @Override
    public Users plantTree(Long userId, Long trees) {

        Users user = userRepository.findById(userId).orElseThrow();
        TreePlantingActivity plantingActivity = treePlantingRepository.findByUsersAndDate(user,LocalDate.now());

        if (plantingActivity!=null) {
            plantingActivity.setNumberOfTrees(plantingActivity.getNumberOfTrees() + trees);
        } else {
            plantingActivity = TreePlantingActivity.builder()
                    .users(user)
                    .date(LocalDate.now())
                    .numberOfTrees(trees)
                    .build();
        }

        user.getTreePlantingActivities().add(plantingActivity);

        return userRepository.save(user);
    }

    @Override
    public CountryChartDataResponse getInformation(String country, Long organizationID) {
        List<Object[]> today = userRepository.findAverageTreesPlantedPerDay();
        List<Object[]> otherDays = userRepository.findTotalTreesPlantedPerDay();
        List<Object[]> avg = userRepository.findAverageTreesPlantedPerDayForCountryAndOrg(country,organizationID);
        List<Object[]> fff = userRepository.findTotalTreesPlantedPerDayForCountryAndOrg(country,organizationID);



        return CountryChartDataResponse.builder()
                .today(convertToTreeDataResponse(today))
                .otherDays(convertToTreeDataResponse(otherDays))
                .findAverageTreesPlantedPerDay(convertToTreeDataResponse(avg))
                .findAverageTreesPlantedPerDayForCountryAndOrg(convertToTreeDataResponse(fff))
                .build();
    }
    private List<TreeDataResponse> convertToTreeDataResponse(List<Object[]> results){
        List<TreeDataResponse> responses = new ArrayList<>();
        for (Object[] result : results) {
            Date date = (Date) result[0];
            Long totalTrees = ((Number) result[1]).longValue(); // Cast to Number and then to Long to handle different numeric types

            TreeDataResponse response = new TreeDataResponse(date, totalTrees);
            responses.add(response);
        }
        return responses;
    }

}
