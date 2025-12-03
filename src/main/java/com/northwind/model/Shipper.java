package com.northwind.model;

public class Shipper {
    private int shipperId;
    private String companyName;
    private String phone;

    public Shipper() {
    }

    public Shipper(int shipperId, String companyName, String phone) {
        this.shipperId = shipperId;
        this.companyName = companyName;
        this.phone = phone;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "\n Shipper: \n" +
                " shipperId = " + shipperId + '\n' +
                " companyName = " + companyName + '\n' +
                " phone = " + phone + '\n';
    }
}