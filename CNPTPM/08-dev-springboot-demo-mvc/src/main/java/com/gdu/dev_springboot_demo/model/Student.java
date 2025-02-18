package com.gdu.dev_springboot_demo.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Student {
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @NotNull(message = "is required")
    @Size(min=1,message = "is required")
    @Pattern(regexp = "^[a-zA-z0-9]{5}", message = "chi nhap chu va so co chieu dai 5 ky tu")
    private int age;
    private String PostCode;
    private String firstName;
    private String lastName;
    private String country;
    private String gender;
    private String language;
    public Student() {
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public String getPostCode() {
        return PostCode;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
