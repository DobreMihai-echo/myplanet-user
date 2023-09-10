package com.myplanet.userservice.service.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.myplanet.userservice.domain.CarbonFootprint;
import com.myplanet.userservice.domain.Users;
import com.myplanet.userservice.payload.CarbonFootprintByMonthPayload;
import com.myplanet.userservice.payload.CarbonFootprintRequest;
import com.myplanet.userservice.repository.CarbonFootprintRepository;
import com.myplanet.userservice.repository.UsersRepository;
import com.myplanet.userservice.service.CarbonFootprintService;
import com.myplanet.userservice.service.UsersService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class CarbonFootprintServiceImpl implements CarbonFootprintService {

    @Autowired
    private CarbonFootprintRepository repository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService service;

    @Override
    public CarbonFootprint saveCarbon(String type, CarbonFootprintRequest carbonRequest) throws IOException, InterruptedException {
        Double co2InKG = 0D;
        Users users = service.getAuthenticatedUser();
        Optional<CarbonFootprint> footprint = repository.findByUsersAndDate(users,LocalDate.now());

        CarbonFootprint footprintToSave = null;
        if (footprint.isPresent()) {
            footprintToSave = footprint.get();
            footprintToSave.setCo2InKG(footprintToSave.getCo2InKG() + co2InKG);
            footprintToSave.setTreeEquivalent(footprintToSave.getTreeEquivalent() + calculateTreeEquivalent(co2InKG));

        } else {
            footprintToSave = CarbonFootprint.builder()
                    .co2InKG(0.0)
                    .treeEquivalent(0.0)
                    .carEmission(0.0)
                    .transitEmission(0.0)
                    .planeEmission(0.0)
                    .energyEmission(0.0)
                    .foodEmission(0.0)
                    .fuelEmission(0.0)
                    .date(LocalDate.now())
                    .users(users)
                    .build();
        }
        switch (type) {
            case "car" :
                co2InKG = this.makeApiCall(URI.create("https://carbonfootprint1.p.rapidapi.com/CarbonFootprintFromCarTravel?distance=" + carbonRequest.getDistance() + "&vehicle=" + carbonRequest.getVehicle()));
                if (footprint.isPresent()) {
                    footprintToSave = footprint.get();
                    footprintToSave.setCarEmission(footprintToSave.getCarEmission() + co2InKG);
                    footprintToSave.setCo2InKG(footprintToSave.getCo2InKG() + co2InKG);
                    footprintToSave.setTreeEquivalent(footprintToSave.getTreeEquivalent() + calculateTreeEquivalent(co2InKG));
                }  else {
                    footprintToSave.setCo2InKG(co2InKG);
                    footprintToSave.setTreeEquivalent(calculateTreeEquivalent(co2InKG));
                    footprintToSave.setCarEmission(co2InKG);
                }
                break;
            case "public":
                co2InKG = this.makeApiCall(URI.create("https://carbonfootprint1.p.rapidapi.com/CarbonFootprintFromPublicTransit?distance="+carbonRequest.getDistance()+"&type=" + carbonRequest.getType()));
                System.out.println("PLUB:" + co2InKG);
                if (footprint.isPresent()) {
                    footprintToSave = footprint.get();
                    footprintToSave.setTransitEmission(footprintToSave.getTransitEmission() + co2InKG);
                    footprintToSave.setCo2InKG(footprintToSave.getCo2InKG() + co2InKG);
                    footprintToSave.setTreeEquivalent(footprintToSave.getTreeEquivalent() + calculateTreeEquivalent(co2InKG));
                }  else {
                    footprintToSave.setCo2InKG(co2InKG);
                    footprintToSave.setTreeEquivalent(calculateTreeEquivalent(co2InKG));
                    footprintToSave.setTransitEmission(co2InKG);
                }
                break;
            case "plane":
                co2InKG = this.makeApiCall(URI.create("https://carbonfootprint1.p.rapidapi.com/CarbonFootprintFromFlight?distance="+carbonRequest.getDistance()+"&type="+carbonRequest.getType()));
                if (footprint.isPresent()) {
                    footprintToSave = footprint.get();
                    footprintToSave.setPlaneEmission(footprintToSave.getPlaneEmission() + co2InKG);
                    footprintToSave.setCo2InKG(footprintToSave.getCo2InKG() + co2InKG);
                    footprintToSave.setTreeEquivalent(footprintToSave.getTreeEquivalent() + calculateTreeEquivalent(co2InKG));
                }  else {
                    footprintToSave.setCo2InKG(co2InKG);
                    footprintToSave.setTreeEquivalent(calculateTreeEquivalent(co2InKG));
                    footprintToSave.setPlaneEmission(co2InKG);
                }
                break;
            case "energy":
                co2InKG = this.makeApiCall(URI.create("https://carbonfootprint1.p.rapidapi.com/CleanHydroToCarbonFootprint?energy="+carbonRequest.getEnergy()+"&consumption="+carbonRequest.getConsumption()));
                if (footprint.isPresent()) {
                    footprintToSave = footprint.get();
                    footprintToSave.setEnergyEmission(footprintToSave.getEnergyEmission() + co2InKG);
                    footprintToSave.setCo2InKG(footprintToSave.getCo2InKG() + co2InKG);
                    footprintToSave.setTreeEquivalent(footprintToSave.getTreeEquivalent() + calculateTreeEquivalent(co2InKG));
                }  else {
                    footprintToSave.setCo2InKG(co2InKG);
                    footprintToSave.setTreeEquivalent(calculateTreeEquivalent(co2InKG));
                    footprintToSave.setEnergyEmission(co2InKG);
                }
                break;
            case "food":

            case "fuel":
                System.out.println("PARAMS:" + "TYPE:" + carbonRequest.getType());
                co2InKG = this.makeApiCall(URI.create("https://carbonfootprint1.p.rapidapi.com/FuelToCO2e?type="+carbonRequest.getType()+"&litres="+carbonRequest.getLiters()));
                if (footprint.isPresent()) {
                    footprintToSave = footprint.get();
                    footprintToSave.setFuelEmission(footprintToSave.getFuelEmission() + co2InKG);
                    footprintToSave.setCo2InKG(footprintToSave.getCo2InKG() + co2InKG);
                    footprintToSave.setTreeEquivalent(footprintToSave.getTreeEquivalent() + calculateTreeEquivalent(co2InKG));
                }  else {
                    footprintToSave.setCo2InKG(co2InKG);
                    footprintToSave.setTreeEquivalent(calculateTreeEquivalent(co2InKG));
                    footprintToSave.setFuelEmission(co2InKG);
                }
                break;
        }
        return repository.save(footprintToSave);
    }

    @Override
    public List<CarbonFootprint> getCarbonFootprintInRange(LocalDate startDate, LocalDate endDate) {
        return repository.findAllByUsersAndDateBetween(service.getAuthenticatedUser(), startDate, endDate);
    }

    @Override
    public List<CarbonFootprintByMonthPayload> getCarbonFootprintByMonth(LocalDate startDate, LocalDate endDate) {
        return repository.findAggregatedByMonthAndYear(startDate,endDate);
    }

    @Override
    public Map<String, Double> getTopEmissionsPercentage(LocalDate startDate, LocalDate endDate) {
        List<CarbonFootprint> rangeCarbon = repository.findAllByUsersAndDateBetween(service.getAuthenticatedUser(),startDate,endDate);
        double totalEmissions = 0;
        Map<String, Double> emissionsByType = new HashMap<>();

        for (CarbonFootprint footprint : rangeCarbon) {
            totalEmissions += footprint.getCo2InKG();
            emissionsByType.put("car", emissionsByType.getOrDefault("car", 0.0) + footprint.getCarEmission());
            emissionsByType.put("transit", emissionsByType.getOrDefault("transit", 0.0) + footprint.getTransitEmission());
            emissionsByType.put("plane", emissionsByType.getOrDefault("plane", 0.0) + footprint.getPlaneEmission());
            emissionsByType.put("energy", emissionsByType.getOrDefault("energy", 0.0) + footprint.getEnergyEmission());
            emissionsByType.put("food", emissionsByType.getOrDefault("food", 0.0) + footprint.getFoodEmission());
            emissionsByType.put("fuel", emissionsByType.getOrDefault("fuel", 0.0) + footprint.getFuelEmission());
        }

        Map<String, Double> percentages = new HashMap<>();
        for (Map.Entry<String,Double> entry : emissionsByType.entrySet()) {
            percentages.put(entry.getKey(), (entry.getValue() / totalEmissions));
        }

        return percentages;
    }

    @Override
    public Double getTotalCarbonEmissionInRange(LocalDate startDate, LocalDate endDate) {
        return repository.getTotalEmissionBetweenDates(startDate, endDate);
    }

    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    private Double calculateTreeEquivalent(Double co2InKG) {
        int treeWeight = 1000;
        int treeDMass = treeWeight / 2;
        int  treeAge = 20;
        double  treeCarb = treeDMass * 0.475;
        double treeCO2 = treeCarb * 3.67;
        double treeDie = treeCO2 / treeAge;
        return co2InKG / treeDie;
    }

    private Double makeApiCall(URI uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("X-RapidAPI-Key", "bc6f7a4864mshda4f8a7095a9527p1871ffjsn11a2716d2559")
                    .header("X-RapidAPI-Host", "carbonfootprint1.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            System.out.println(request.uri());
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            System.out.println("MY RESPONSE BODY:" + response.body());
            // Get the double value associated with the key "carbonEquivalent"
            return jsonObject.getDouble("carbonEquivalent");
        } catch (IOException | InterruptedException ex) {
            System.out.println("Exception");
        }
        return null;

    }
}
