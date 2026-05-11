package com.example.busticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TicketLookupRequest {

    @NotBlank(message = "Ma ve khong duoc de trong")
    @Size(max = 20, message = "Ma ve toi da 20 ky tu")
    private String ticketCode;

    @NotBlank(message = "So dien thoai khong duoc de trong")
    @Size(max = 15, message = "So dien thoai toi da 15 ky tu")
    private String passengerPhone;

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }
}
