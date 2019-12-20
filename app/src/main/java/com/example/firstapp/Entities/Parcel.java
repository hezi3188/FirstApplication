package com.example.firstapp.Entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class Parcel {
    private long parcelID;
    private ParcelType parcelType;
    private boolean isFragile;
    private ParcelWeight parcelWeight;
    private double Latitude;
    private double Longitude;
    private Date deliveryParcelDate;
    private Date getParcelDate;
    private ParcelStatus status;
    private String deliveryName;
    private String customerId;




    //-------------Ctors--------------------//
    public Parcel() {

        this.status = ParcelStatus.SENT;
        this.deliveryName = "NO";
        this.getParcelDate = new Date(System.currentTimeMillis());
    }



//-------------------------------------//


    //--------------Ges&Set-----------------//

    public void setParcelID(long parcelID) {
        this.parcelID = parcelID;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public long getParcelID() {
        return parcelID;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

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

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        this.Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        this.Longitude = longitude;
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

    public ParcelStatus getStatus() {
        return status;
    }

    public void setStatus(ParcelStatus status) {
        this.status = status;
    }

    //------------------------------------------//

    @Override
    public String toString() {
        return "Parcel{" +
                ", parcelID=" + parcelID +
                ", parcelType=" + parcelType +
                ", isFragile=" + isFragile +
                ", parcelWeight=" + parcelWeight +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", deliveryParcelDate=" + deliveryParcelDate +
                ", getParcelDate=" + getParcelDate +
                ", status=" + status +
                ", deliveryName='" + deliveryName + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
