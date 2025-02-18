package com.firstDemo.pring_boot_demo.model;

import jakarta.validation.constraints.*;

public class Customer {
    @NotNull(message = "Tên công ty là bắt buộc")
    @Size(min = 1, message = "Tên công ty là bắt buộc")
    private String nameOfCompany;

    @NotNull(message = "Địa chỉ đường là bắt buộc")
    @Size(min = 1, message = "Địa chỉ đường là bắt buộc")
    private String streetAddress;

    @NotNull(message = "Thành phố là bắt buộc")
    @Size(min = 1, message = "Thành phố là bắt buộc")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Thành phố không được chứa số.")
    private String city;

    @NotNull(message = "Quốc gia là bắt buộc")
    @Size(min = 1, message = "Quốc gia là bắt buộc")
    private String country;

    @NotNull(message = "Bắt buộc phải có PostalCode")
    @Size(min = 1, message = "Bắt buộc phải có PostalCode")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9\\-\\s]+$", message = "PostalCode phải chứa cả chữ cái và số.")
    private String postalCode;

    @NotNull(message = "Bắt buộc phải có Region")
    @Size(min = 1, message = "Bắt buộc phải có Region")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Region không được chứa số.")
    private String region;

    public Customer(String nameOfCompany, String streetAddress, String city, String region, String postalCode, String country) {
        this.nameOfCompany = nameOfCompany;
        this.streetAddress = streetAddress;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Customer() {

    }

    public String getNameOfCompany() {
        return nameOfCompany;
    }

    public void setNameOfCompany(String nameOfCompany) {
        this.nameOfCompany = nameOfCompany;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
