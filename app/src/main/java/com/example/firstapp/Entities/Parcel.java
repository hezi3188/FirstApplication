package com.example.firstapp.Entities;

import android.location.Location;
import android.provider.ContactsContract;


import java.util.Date;

;
;
public class Parcel {
    private ParcelType parcelType;
    private boolean isFragile;
    private ParcelWeight parcelWeight;
    private Location storageLocation;
    private String recipientName;
    private String recipientAddress;
    private Date deliveryParcelDate;
    private Date getParcelDate;
    private String phoneNumber;
    private ContactsContract.CommonDataKinds.Email recipientEmail;
    private ParcelStatus status;
    private String deliveryName;

    //-------------Ctors--------------------//
    public Parcel() {
        this.status = ParcelStatus.SENT;
        this.deliveryName = "NO";
    }

    public Parcel(ParcelType parcelType, boolean isFragile, ParcelWeight parcelWeight, Location storageLocation, String recipientName, String recipientAddress, Date deliveryParcelDate, Date getParcelDate, String phoneNumber, ContactsContract.CommonDataKinds.Email recipientEmail, ParcelStatus status) {
        this.parcelType = parcelType;
        this.isFragile = isFragile;
        this.parcelWeight = parcelWeight;
        this.storageLocation = storageLocation;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.deliveryParcelDate = deliveryParcelDate;
        this.getParcelDate = getParcelDate;
        this.phoneNumber = phoneNumber;
        this.recipientEmail = recipientEmail;
        this.status = status;
    }

    //-------------------------------------//


    //--------------Ges&Set-----------------//

    public ParcelType getParcelType() {
        return parcelType;
    }

    public void setParcelType(ParcelType parcelType) {
        this.parcelType = parcelType;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public void setFragile(boolean fragile) {
        isFragile = fragile;
    }

    public ParcelWeight getParcelWeight() {
        return parcelWeight;
    }

    public void setParcelWeight(ParcelWeight parcelWeight) {
        this.parcelWeight = parcelWeight;
    }

    public Location getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(Location storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public Date getDeliveryParcelDate() {
        return deliveryParcelDate;
    }

    public void setDeliveryParcelDate(Date deliveryParcelDate) {
        this.deliveryParcelDate = deliveryParcelDate;
    }

    public Date getGetParcelDate() {
        return getParcelDate;
    }

    public void setGetParcelDate(Date getParcelDate) {
        this.getParcelDate = getParcelDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ContactsContract.CommonDataKinds.Email getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(ContactsContract.CommonDataKinds.Email recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    public void setStatus(ParcelStatus status) {
        this.status = status;
    }

    //------------------------------------------//
}
