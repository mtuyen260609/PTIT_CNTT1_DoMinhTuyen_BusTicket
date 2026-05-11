package com.example.busticket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookingRequest {

    @NotNull(message = "Vui long chon ghe")
    private Long seatId;

    @NotBlank(message = "Ho ten khong duoc de trong")
    @Size(max = 100, message = "Ho ten toi da 100 ky tu")
    private String passengerName;

    @NotBlank(message = "So dien thoai khong duoc de trong")
    @Size(max = 15, message = "So dien thoai toi da 15 ky tu")
    private String passengerPhone;

    @Email(message = "Email khong hop le")
    @Size(max = 100, message = "Email toi da 100 ky tu")
    private String passengerEmail;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }
}
