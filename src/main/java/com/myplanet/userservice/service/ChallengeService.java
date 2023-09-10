package com.myplanet.userservice.service;

import com.myplanet.userservice.domain.Challenge;
import com.myplanet.userservice.domain.ChallengeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChallengeService {
    Challenge saveChallenge(String type, ChallengeRequest challenge);

    List<Challenge> getAllChallenges(String username);

    Challenge getChallengeById(Long id);
    String updateChallenge(Challenge challenge);

    String deleteChallenge(Long id) ;

    void joinChallenge(List<Long> challengeId, String username);

    void completeChallenge(Long challengeId, String username);

    void unjoinChallenge(Long challengeId, String username);

    List<Challenge> getOngoingChallengesForUser(String username) ;

    List<Challenge> getCompletedChallengesForUser(String username);
}
