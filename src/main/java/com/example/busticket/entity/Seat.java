package com.example.busticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(
        name = "seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"trip_id", "seat_number"})
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "seat_row")
    private Integer seatRow;

    @Column(name = "seat_column")
    private Integer seatColumn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    public Long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(Integer seatRow) {
        this.seatRow = seatRow;
    }

    public Integer getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(Integer seatColumn) {
        this.seatColumn = seatColumn;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }
}
