package com.example.busticket.dto;

import com.example.busticket.entity.BusTypeOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BusRequest {

    @NotBlank(message = "Biển số xe không được để trống")
    @Size(max = 20, message = "Biển số xe tối đa 20 ký tự")
    private String licensePlate;

    @NotNull(message = "Loại xe không được để trống")
    private BusTypeOption busType;

    @Size(max = 100, message = "Tên hãng xe tối đa 100 ký tự")
    private String company;

    @Size(max = 100, message = "Tên tài xế tối đa 100 ký tự")
    private String driverName;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public BusTypeOption getBusType() {
        return busType;
    }

    public void setBusType(BusTypeOption busType) {
        this.busType = busType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
