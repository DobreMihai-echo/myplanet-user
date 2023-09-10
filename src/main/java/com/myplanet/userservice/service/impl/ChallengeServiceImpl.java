package com.myplanet.userservice.service.impl;

import com.myplanet.userservice.domain.Challenge;
import com.myplanet.userservice.domain.ChallengeRequest;
import com.myplanet.userservice.repository.ChallengeRepository;
import com.myplanet.userservice.service.ChallengeService;
import com.myplanet.userservice.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired
    private ChallengeRepository repository;

    @Autowired
    private UsersService usersService;

    public Challenge saveChallenge(String type,ChallengeRequest challenge) {
        System.out.println("Challenge");
        return repository.save(Challenge.builder()
                .creator(usersService.getAuthenticatedUser())
                .level(challenge.getPoints()<30?"Easy":challenge.getPoints()<70?"Medium":"Hard")
                .challengeTags(challenge.getChallengeTags())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .build());
    }

    public List<Challenge> getAllChallenges(String username) {

        List<Challenge> challenges = repository.findAll();
        return challenges.stream().filter(challenge -> !challenge.getChallengeJoiners().contains(username) && !challenge.getCompletedJoiners().contains(username)).collect(Collectors.toList());
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

    public void joinChallenge(List<Long> challengeId, String username) {
        List<Challenge> knownOngoingChallenges = repository.findAll();
        knownOngoingChallenges = knownOngoingChallenges.stream().filter(challenge -> challenge.getChallengeJoiners().contains(username)).collect(Collectors.toList());
        for (Challenge knownChallenges: knownOngoingChallenges) {
            if (!challengeId.contains(knownChallenges.getId())) {
                knownChallenges.getChallengeJoiners().remove(username);
                repository.save(knownChallenges);
            }
        }
        for (Long id: challengeId) {
            Challenge challenge = repository.findById(id).orElseThrow(RuntimeException::new);
            if (!challenge.getChallengeJoiners().contains(username)) {
                challenge.getChallengeJoiners().add(usersService.getAuthenticatedUser());
                repository.save(challenge);
            }
        }
    }

    public void completeChallenge(Long challengeId, String username) {
        Challenge challenge = repository.findById(challengeId).orElseThrow(RuntimeException::new);
        challenge.getChallengeJoiners().remove(username);
        challenge.getCompletedJoiners().add(usersService.getAuthenticatedUser());
        repository.save(challenge);
    }

    public void unjoinChallenge(Long challengeId, String username) {
        Challenge challenge = repository.findById(challengeId).orElseThrow(RuntimeException::new);
        challenge.getChallengeJoiners().remove(username);
        repository.save(challenge);
    }

    public List<Challenge> getOngoingChallengesForUser(String username) {
        List<Challenge> allChallenges = repository.findAll();
        return allChallenges.stream().filter(challenge -> challenge.getChallengeJoiners().contains(username)).collect(Collectors.toList());
    }

    public List<Challenge> getCompletedChallengesForUser(String username) {
        List<Challenge> allChallenges = repository.findAll();
        return allChallenges.stream().filter(challenge -> challenge.getCompletedJoiners().contains(username)).collect(Collectors.toList());
    }
}
