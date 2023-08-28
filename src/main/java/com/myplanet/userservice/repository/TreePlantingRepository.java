package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.TreePlantingActivity;
import com.myplanet.userservice.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TreePlantingRepository extends JpaRepository<TreePlantingActivity,Long> {
    TreePlantingActivity findByUsersAndDate(Users user, LocalDate date);

}
