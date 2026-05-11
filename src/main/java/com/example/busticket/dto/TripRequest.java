package com.example.busticket.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public class TripRequest {

    @NotNull(message = "Diem di khong duoc de trong")
    private Long departureId;

    @NotNull(message = "Diem den khong duoc de trong")
    private Long arrivalId;

    @NotNull(message = "Xe khong duoc de trong")
    private Long busId;

    @NotNull(message = "Gio khoi hanh khong duoc de trong")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;

    @NotNull(message = "Gia ve khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Gia ve phai lon hon 0")
    private BigDecimal price;

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

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
