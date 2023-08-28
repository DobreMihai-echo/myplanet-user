package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.TreePlantingActivity;
import com.myplanet.userservice.domain.UsersBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TreePlantingRepository extends JpaRepository<TreePlantingActivity,Long> {
    TreePlantingActivity findByUsersAndDate(UsersBase user, LocalDate date);

}
