package com.myplanet.userservice.service;

import com.myplanet.userservice.domain.Challenge;
import com.myplanet.userservice.domain.ChallengeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChallengeService {
    Challenge saveChallenge(Boolean isOrganizationLevel, ChallengeRequest challenge);

    List<Challenge> getAllChallenges(Boolean isOrganizationLevel);

    Challenge getChallengeById(Long id);
    String updateChallenge(Challenge challenge);

    String deleteChallenge(Long id) ;

    void joinChallenge(List<Long> challengeId);

    void completeChallenge(Long challengeId);

    void unjoinChallenge(Long challengeId, String username);

    List<Challenge> getOngoingChallengesForUser(Boolean isOrganizationLevel) ;

    List<Challenge> getCompletedChallengesForUser(String username);

    List<Challenge> getChallengesAtOrganizationLevel(String organizationName);

}
