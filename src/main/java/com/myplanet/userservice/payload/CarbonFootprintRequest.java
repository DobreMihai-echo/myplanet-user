package com.myplanet.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonFootprintRequest {
    private String distance;
    private String vehicle;
    private String type;
    private String energy;
    private String consumption;
    private String liters;
}
