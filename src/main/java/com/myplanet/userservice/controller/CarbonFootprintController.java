package com.myplanet.userservice.controller;

import com.myplanet.userservice.payload.CarbonFootprintRequest;
import com.myplanet.userservice.service.CarbonFootprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/footprint")
@CrossOrigin()
public class CarbonFootprintController {

    @Autowired
    private CarbonFootprintService service;

    @PostMapping
    public ResponseEntity<?> calculateFootprint(@RequestParam(name = "type") String type, @RequestBody CarbonFootprintRequest request) throws IOException, InterruptedException {
        return ResponseEntity.ok(service.saveCarbon(type,request));
    }

    @GetMapping("/emissions")
    public ResponseEntity<?> getEmissionsByUserAndDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // Convert startDate and endDate from String to LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return ResponseEntity.ok(service.getCarbonFootprintInRange( start, end));
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> getEmissionsByUserAndDateRangeMonth(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // Convert startDate and endDate from String to LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return ResponseEntity.ok(service.getCarbonFootprintByMonth( start, end));
    }

    @GetMapping("/topEmissions")
    public ResponseEntity<?> getTopEmissions(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return ResponseEntity.ok(service.getTopEmissionsPercentage(start,end));
    }

    @GetMapping("/totalEmission")
    public ResponseEntity<?> getTotalCarbonEmissionInRange(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return ResponseEntity.ok(service.getTotalCarbonEmissionInRange(start,end));
    }

    @DeleteMapping("/emissions")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        service.deleteByID(id);
        return ResponseEntity.ok("Deleted");
    }
}
