package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.ERole;
import com.myplanet.userservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Role,Long> {
    Role findByName(ERole name);
    Boolean existsByName(ERole name);
}
