package com.myplanet.userservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "carbon")
public class CarbonFootprint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;
    private Double co2InKG;

    private Double treeEquivalent;
    private Double carEmission;
    private Double transitEmission;
    private Double planeEmission;
    private Double energyEmission;
    private Double foodEmission;
    private Double fuelEmission;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private Users users;


}
