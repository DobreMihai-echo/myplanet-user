package com.myplanet.userservice.repository;

import com.myplanet.userservice.domain.CarbonFootprint;
import com.myplanet.userservice.domain.Users;
import com.myplanet.userservice.payload.CarbonFootprintByMonthPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarbonFootprintRepository extends JpaRepository<CarbonFootprint,Long> {

    Optional<CarbonFootprint> findByUsersAndDate(Users users, LocalDate date);

    List<CarbonFootprint> findAllByUsersAndDateBetween(Users users, LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.myplanet.userservice.payload.CarbonFootprintByMonthPayload( " +
            "FUNCTION('MONTH', date) as month, " +
            "FUNCTION('YEAR', date) as year, " +
            "SUM(carEmission), SUM(transitEmission), SUM(planeEmission), SUM(energyEmission), " +
            "SUM(foodEmission), SUM(fuelEmission)) " +
            "FROM CarbonFootprint " +
            "WHERE date BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('MONTH', date), FUNCTION('YEAR', date)")
    List<CarbonFootprintByMonthPayload> findAggregatedByMonthAndYear(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(co2InKG) FROM CarbonFootprint  WHERE date BETWEEN :startDate AND :endDate")
    Double getTotalEmissionBetweenDates(LocalDate startDate, LocalDate endDate);
}
