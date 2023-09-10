package com.myplanet.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarbonFootprintByMonthPayload {
    private Integer month;
    private Integer year;
    private Double carEmission;
    private Double transitEmission;
    private Double planeEmission;
    private Double energyEmission;
    private Double foodEmission;
    private Double fuelEmission;
}
