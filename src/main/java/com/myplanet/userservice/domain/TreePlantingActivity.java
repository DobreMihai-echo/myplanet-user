package com.myplanet.userservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TreePlantingActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    @JsonIgnore
    private Users users;

    @ManyToOne
    @JoinColumn(name = "organizationId", nullable = false)
    @JsonBackReference
    private Organization organization;

    private LocalDate date;

    private Long numberOfTrees;
}
