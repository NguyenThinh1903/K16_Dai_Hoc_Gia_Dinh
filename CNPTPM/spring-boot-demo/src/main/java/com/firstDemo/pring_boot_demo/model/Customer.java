package com.firstDemo.pring_boot_demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "Tên công ty là bắt buộc")
    @Size(min = 1, message = "Tên công ty là bắt buộc")
    @Column(name = "name_of_company")
    private String nameOfCompany;

    @NotNull(message = "Địa chỉ đường là bắt buộc")
    @Size(min = 1, message = "Địa chỉ đường là bắt buộc")
    @Column(name = "street_address")
    private String streetAddress;

    @NotNull(message = "Thành phố là bắt buộc")
    @Size(min = 1, message = "Thành phố là bắt buộc")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Thành phố không được chứa số")
    @Column(name = "city")
    private String city;

    @NotNull(message = "Quốc gia là bắt buộc")
    @Size(min = 1, message = "Quốc gia là bắt buộc")
    @Column(name = "country")
    private String country;

    @NotNull(message = "Mã bưu điện là bắt buộc")
    @Size(min = 1, message = "Mã bưu điện là bắt buộc")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9\\-\\s]+$", message = "Mã bưu điện phải chứa cả chữ cái và số")
    @Column(name = "postal_code")
    private String postalCode;

    @NotNull(message = "Vùng là bắt buộc")
    @Size(min = 1, message = "Vùng là bắt buộc")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Vùng không được chứa số")
    @Column(name = "region")
    private String region;

    public Customer(int id, String nameOfCompany, String streetAddress, String city, String region, String postalCode, String country) {
        this.id = id;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}