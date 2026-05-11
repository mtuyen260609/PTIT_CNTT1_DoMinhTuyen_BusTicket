package com.example.busticket.dto;

import com.example.busticket.entity.SeatStatus;

public class SeatView {

    private final Long id;
    private final String seatNumber;
    private final Integer seatRow;
    private final Integer seatColumn;
    private final SeatStatus status;

    public SeatView(Long id, String seatNumber, Integer seatRow, Integer seatColumn, SeatStatus status) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public Integer getSeatRow() {
        return seatRow;
    }

    public Integer getSeatColumn() {
        return seatColumn;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }
}
