package com.myplanet.userservice.service.impl;

import com.myplanet.userservice.domain.*;
import com.myplanet.userservice.repository.ChallengeRepository;
import com.myplanet.userservice.repository.OrganizationMemberRepository;
import com.myplanet.userservice.repository.UsersRepository;
import com.myplanet.userservice.service.ChallengeService;
import com.myplanet.userservice.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired
    private ChallengeRepository repository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    public Challenge saveChallenge(Boolean isOrganizationLevel,ChallengeRequest challenge) {
        if (isOrganizationLevel) {
            Organization organization = usersService.getAuthenticatedUser().getOrganizationJoiningActivities().get(0).getOrganization();
            return repository.save(Challenge.builder()
                            .creator(usersService.getAuthenticatedUser())
                            .organization(organization)
                            .points(challenge.getPoints())
                            .title(challenge.getTitle())
                            .description(challenge.getDescription())
                            .level(challenge.getPoints()<30?"Easy":challenge.getPoints()<70?"Medium":"Hard")
                    .build());
        }
        return repository.save(Challenge.builder()
                .creator(usersService.getAuthenticatedUser())
                .level(challenge.getPoints()<30?"Easy":challenge.getPoints()<70?"Medium":"Hard")
                .challengeTags(challenge.getChallengeTags())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .build());
    }

    public List<Challenge> getAllChallenges(Boolean isOrganizationLevel) {

        if (isOrganizationLevel) {
            return repository.findUnjoinedChallengesByOrganization(usersService.getAuthenticatedUser(),usersService.getAuthenticatedUser().getOrganizationJoiningActivities().get(0).getOrganization().getOrganizationName());
        }
        return repository.findUnjoinedChallengesWithoutOrganization(usersService.getAuthenticatedUser());
    }

    public Challenge getChallengeById(Long id) {
        return repository.findById(id).get();
    }

    public String updateChallenge(Challenge challenge) {
        Challenge challengeToUpdate = repository.findById(challenge.getId()).get();

        challengeToUpdate.setTitle(challenge.getTitle());
        challengeToUpdate.setDescription(challenge.getDescription());
        challengeToUpdate.setLevel(challenge.getLevel());
        challengeToUpdate.setPoints(challenge.getPoints());

        try {
            repository.save(challenge);
            return "Challenge was updated successfully";
        } catch (Exception ex) {
            return "There was an error updating the challenge";
        }
    }

    public String deleteChallenge(Long id) {
        try {
            repository.deleteById(id);
            return "Challenge deleted successfully";
        } catch (Exception ex) {
            return "There was an error deleting the challenge";
        }
    }

    public void joinChallenge(List<Long> challengeId) {
        for (Long id: challengeId) {
            Challenge challenge = repository.findById(id).orElseThrow(RuntimeException::new);
            if (!challenge.getChallengeJoiners().contains(usersService.getAuthenticatedUser())) {
                challenge.getChallengeJoiners().add(usersService.getAuthenticatedUser());
                repository.save(challenge);
            }
        }
    }

    public void completeChallenge(Long challengeId) {
        Challenge challenge = repository.findById(challengeId).orElseThrow(RuntimeException::new);
        challenge.getChallengeJoiners().remove(usersService.getAuthenticatedUser());
        challenge.getCompletedJoiners().add(usersService.getAuthenticatedUser());
        if (challenge.getOrganization() != null) {
            OrganizationMemberPoints organizationMemberPoints = organizationMemberRepository.getOrganizationMemberPointsByUsersAndOrganization(usersService.getAuthenticatedUser(),challenge.getOrganization());
            if (organizationMemberPoints == null) {
                organizationMemberPoints = OrganizationMemberPoints
                        .builder()
                        .organization(challenge.getOrganization())
                        .points(0L)
                        .pointsMonth(0L)
                        .pointsYear(0L)
                        .users(usersService.getAuthenticatedUser())
                        .lastUpdated(LocalDate.now())
                        .build();
            }
            addPoints(organizationMemberPoints,challenge.getPoints());
            organizationMemberRepository.save(organizationMemberPoints);
        }
        Users authenticatedUser = usersService.getAuthenticatedUser();
        authenticatedUser.setPoints(authenticatedUser.getPoints() + challenge.getPoints());
        usersRepository.save(authenticatedUser);
        repository.save(challenge);
    }

    public void unjoinChallenge(Long challengeId, String username) {
        Challenge challenge = repository.findById(challengeId).orElseThrow(RuntimeException::new);
        challenge.getChallengeJoiners().remove(username);
        repository.save(challenge);
    }

    public List<Challenge> getOngoingChallengesForUser(Boolean isOrganizationLevel) {
        if (isOrganizationLevel) {
            System.out.println("CURRENTLY AUTHENTICATED USER:" + usersService.getAuthenticatedUser());
            return repository.findNotCompletedChallengesByUserAndOrganization(usersService.getAuthenticatedUser(),usersService.getAuthenticatedUser().getOrganizationJoiningActivities().get(0).getOrganization());
        }
        return repository.findNotCompletedChallengesByUserWithoutOrganization(usersService.getAuthenticatedUser());
    }

    public List<Challenge> getCompletedChallengesForUser(String username) {
        List<Challenge> allChallenges = repository.findAll();
        return allChallenges.stream().filter(challenge -> challenge.getCompletedJoiners().contains(username)).collect(Collectors.toList());
    }

    @Override
    public List<Challenge> getChallengesAtOrganizationLevel(String organizationName) {
        return repository.findUnjoinedChallengesByOrganization(usersService.getAuthenticatedUser(),organizationName);
    }

    public void addPoints(OrganizationMemberPoints organizationMemberPoints, Long newPoints) {
        LocalDate today = LocalDate.now();

        System.out.println("ORG" + organizationMemberPoints);
        if (organizationMemberPoints.getLastUpdated()!= null && !today.equals(organizationMemberPoints.getLastUpdated())) {
            if (today.getYear() != organizationMemberPoints.getLastUpdated().getYear()) {
                organizationMemberPoints.setPointsYear(0L);
            }

            if (today.getMonthValue() != organizationMemberPoints.getLastUpdated().getMonthValue()) {
                organizationMemberPoints.setPointsMonth(0L);
            }
            organizationMemberPoints.setPoints(0L);
        }

        organizationMemberPoints.setPoints(organizationMemberPoints.getPoints() + newPoints);
        organizationMemberPoints.setPointsMonth(organizationMemberPoints.getPointsMonth() + newPoints);
        organizationMemberPoints.setPointsYear(organizationMemberPoints.getPointsYear() + newPoints);

        organizationMemberPoints.setLastUpdated(today);
        organizationMemberRepository.save(organizationMemberPoints);
    }
}
