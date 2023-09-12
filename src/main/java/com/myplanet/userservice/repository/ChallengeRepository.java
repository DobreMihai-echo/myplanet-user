package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.Challenge;
import com.myplanet.userservice.domain.Organization;
import com.myplanet.userservice.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {

    @Query("SELECT c FROM Challenge c WHERE :user NOT MEMBER OF c.challengeJoiners AND c.organization.organizationName = :orgName")
    List<Challenge> findUnjoinedChallengesByOrganization(@Param("user") Users user, @Param("orgName") String orgName);

    @Query("SELECT c FROM Challenge c WHERE :user NOT MEMBER OF c.challengeJoiners AND c.organization IS NULL")
    List<Challenge> findUnjoinedChallengesWithoutOrganization(@Param("user") Users user);

    @Query("SELECT c FROM Challenge c WHERE c.organization IS NULL")
    List<Challenge> findChallengesWithoutOrganization();

    @Query("SELECT c FROM Challenge c WHERE :user NOT MEMBER OF c.completedJoiners AND c.organization = :organization")
    List<Challenge> findNotCompletedChallengesByUserAndOrganization(@Param("user") Users user, @Param("organization") Organization organization);

    @Query("SELECT c FROM Challenge c WHERE :user NOT MEMBER OF c.completedJoiners AND c.organization IS NULL")
    List<Challenge> findNotCompletedChallengesByUserWithoutOrganization(@Param("user") Users user);

}
