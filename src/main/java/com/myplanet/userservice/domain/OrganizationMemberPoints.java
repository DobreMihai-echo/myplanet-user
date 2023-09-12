package com.myplanet.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "organization_member_points")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationMemberPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(name = "points")
    private Long points;

    @Column(name = "points_month")
    private Long pointsMonth;

    @Column(name = "points_year")
    private Long pointsYear;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;
}
