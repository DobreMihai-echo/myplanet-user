package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
}
