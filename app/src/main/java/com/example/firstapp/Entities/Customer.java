package com.example.firstapp.Entities;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private long id;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private ContactsContract.CommonDataKinds.Email email;
    private String password;
    private List<Parcel> parcels;

    public Customer() {
        parcels=new ArrayList<Parcel>();
    }

    //---------Methods-------------//
    public void addParcel(Parcel parcel){
        parcels.add(parcel);
    }

    //----------Get&Set-------------//

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ContactsContract.CommonDataKinds.Email getEmail() {
        return email;
    }

    public void setEmail(ContactsContract.CommonDataKinds.Email email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Parcel> getParcels() {
        return parcels;
    }

    public void setParcels(List<Parcel> parcels) {
        this.parcels = parcels;
    }
}
