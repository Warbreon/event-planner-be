package com.cognizant.EventPlanner.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "building")
    private String building;

    @Column(name = "zip")
    private String zip;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<Event> events;
}
