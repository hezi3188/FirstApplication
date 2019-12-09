package com.example.firstapp.Entities;

import android.provider.ContactsContract;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String street;
    private int buildingNumber;
    private int postalAddress;
    private String phoneNumber;
    private String email;
    private List<Parcel> parcels;

    public Customer() {
        parcels=new ArrayList<Parcel>();
    }

    //---------Methods-------------//
    public void addParcel(Parcel parcel){
        parcels.add(parcel);
    }

    //----------Get&Set-------------//

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public int getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(int postalAddress) {
        this.postalAddress = postalAddress;
    }
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Parcel> getParcels() {
        return parcels;
    }

    public void setParcels(List<Parcel> parcels) {
        this.parcels = parcels;
    }
}
