package com.example.busticket.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TripSearchRequest {

    @NotNull(message = "Diem di khong duoc de trong")
    private Long departureId;

    @NotNull(message = "Diem den khong duoc de trong")
    private Long arrivalId;

    @NotNull(message = "Ngay di khong duoc de trong")
    private LocalDate departureDate;

    public Long getDepartureId() {
        return departureId;
    }

    public void setDepartureId(Long departureId) {
        this.departureId = departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(Long arrivalId) {
        this.arrivalId = arrivalId;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}
