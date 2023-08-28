package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.Users;
import com.myplanet.userservice.payload.TreeDataResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Boolean existsByUsername(String username);

    List<Users> findUsersByUsername(String name, Pageable pageable);

    List<Users> findUsersByFollowingUsers(Users user, Pageable pageable);
    List<Users> findUsersByFollowerUsers(Users user, Pageable pageable);

    List<Users> findByUsernameIn(List<String> username);

    @Query("SELECT j FROM Users u JOIN u.joiners j WHERE u.id = :organizationId AND j.country = :country")
    List<Users> findJoinersByOrganizationAndCountry(@Param("organizationId") Long organizationId, @Param("country") String country);

    @Query(value="SELECT DATE(date) as planting_date, SUM(number_of_trees) as total_trees FROM tree_planting_activity GROUP BY DATE(date)", nativeQuery=true)
    List<Object[]> findTotalTreesPlantedPerDay();



    @Query(value="SELECT DATE(t.date) as planting_date, SUM(t.number_of_trees) as total_trees FROM tree_planting_activity t JOIN users u ON t.id = u.id WHERE u.country = :country AND u.id = :orgId GROUP BY DATE(t.date)", nativeQuery=true)
    List<Object[]> findTotalTreesPlantedPerDayForCountryAndOrg(@Param("country") String country, @Param("orgId") Long orgId);

    @Query(value="SELECT DATE(date) as planting_date, AVG(number_of_trees) as avg_trees FROM tree_planting_activity GROUP BY DATE(date)", nativeQuery=true)
    List<Object[]> findAverageTreesPlantedPerDay();

    @Query(value="SELECT DATE(t.date) as planting_date, AVG(t.number_of_trees) as avg_trees FROM tree_planting_activity t JOIN users u ON t.id = u.id WHERE u.country = :country AND u.id = :orgId GROUP BY DATE(t.date)", nativeQuery=true)
    List<Object[]> findAverageTreesPlantedPerDayForCountryAndOrg(@Param("country") String country, @Param("orgId") Long orgId);
}
