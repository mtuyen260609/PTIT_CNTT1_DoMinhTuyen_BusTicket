package com.example.busticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "routes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"departure_id", "arrival_id"})
)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departure_id", nullable = false)
    private Location departure;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arrival_id", nullable = false)
    private Location arrival;

    @Column
    private Integer distance;

    public Long getId() {
        return id;
    }

    public Location getDeparture() {
        return departure;
    }

    public void setDeparture(Location departure) {
        this.departure = departure;
    }

    public Location getArrival() {
        return arrival;
    }

    public void setArrival(Location arrival) {
        this.arrival = arrival;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
