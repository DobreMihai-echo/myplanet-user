package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.Users;
import com.myplanet.userservice.domain.UsersBase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBaseRepository extends JpaRepository<UsersBase, Long> {
    Optional<UsersBase> findByUsername(String username);
    Boolean existsByUsername(String username);

    List<UsersBase> findUsersByUsername(String name, Pageable pageable);
    List<UsersBase> findByUsernameIn(List<String> username);
}
