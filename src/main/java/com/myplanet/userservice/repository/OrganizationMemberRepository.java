package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.Organization;
import com.myplanet.userservice.domain.OrganizationMemberPoints;
import com.myplanet.userservice.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMemberPoints,Long> {

    OrganizationMemberPoints getOrganizationMemberPointsByUsersAndOrganization(Users users, Organization organization);
    List<OrganizationMemberPoints> findByOrganization_OrganizationNameOrderByPoints(String organizationName);
}
