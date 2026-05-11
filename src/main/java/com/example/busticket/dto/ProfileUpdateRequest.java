package com.example.busticket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProfileUpdateRequest {

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9+\\-\\s]{9,15}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    @Size(max = 500, message = "Địa chỉ tối đa 500 ký tự")
    private String address;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
