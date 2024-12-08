package com.gundemgaming.fukantin.dto.user;

public class UpdateUserRequest {

    private String department;
    private String instagramAddress;
    private String biography;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String department, String instagramAddress, String biography) {
        this.department = department;
        this.instagramAddress = instagramAddress;
        this.biography = biography;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInstagramAddress() {
        return instagramAddress;
    }

    public void setInstagramAddress(String instagramAddress) {
        this.instagramAddress = instagramAddress;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

}
