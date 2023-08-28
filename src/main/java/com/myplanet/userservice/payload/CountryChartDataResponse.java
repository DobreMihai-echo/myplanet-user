package com.myplanet.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryChartDataResponse {
    List<TreeDataResponse> today;
    List<TreeDataResponse> otherDays;
    List<TreeDataResponse> findAverageTreesPlantedPerDay;
    List<TreeDataResponse> findAverageTreesPlantedPerDayForCountryAndOrg;
}
