package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.Users;
import com.myplanet.userservice.domain.UsersBase;
import com.myplanet.userservice.payload.OrganizationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    List<Users> findUsersByFollowingUsers(UsersBase user, Pageable pageable);
    List<Users> findUsersByFollowerUsers(UsersBase user, Pageable pageable);

    Optional<Users> findByUsername(String username);
    Boolean existsByUsername(String username);

    List<Users> findUsersByUsername(String name, Pageable pageable);
    List<Users> findByUsernameIn(List<String> username);

    @Query("SELECT new com.myplanet.userservice.payload.OrganizationDTO(o.id, o.organizationName) FROM OrganizationJoiningActivity oja JOIN oja.organization o WHERE oja.users.username = :username")
    List<OrganizationDTO> findOrganizationNamesByUsername(String username);

}
