package com.myplanet.userservice.service;

import com.myplanet.userservice.domain.CarbonFootprint;
import com.myplanet.userservice.payload.CarbonFootprintByMonthPayload;
import com.myplanet.userservice.payload.CarbonFootprintRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CarbonFootprintService {

    CarbonFootprint saveCarbon(String type, CarbonFootprintRequest request) throws IOException, InterruptedException;

    List<CarbonFootprint> getCarbonFootprintInRange(LocalDate startDate, LocalDate endDate);

    List<CarbonFootprintByMonthPayload> getCarbonFootprintByMonth(LocalDate startDate, LocalDate endDate);

    Map<String, Double> getTopEmissionsPercentage(LocalDate startDate, LocalDate endDate);
    Double getTotalCarbonEmissionInRange(LocalDate startDate, LocalDate endDate);
    void deleteByID(Long id);
}
