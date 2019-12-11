package com.example.firstapp.Entities;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class Parcel {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private long parcelID;
    private ParcelType parcelType;
    private boolean isFragile;
    private ParcelWeight parcelWeight;
    private Location storageLocation;
    private Date deliveryParcelDate;
    private Date getParcelDate;
    private ParcelStatus status;
    private String deliveryName;
    private String customerId;
    private static int id ;


    //-------------Ctors--------------------//
    public Parcel() {
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("properties");
        reference.child("parcelId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parcelID=dataSnapshot.getValue(long.class);
                reference.child("parcelId").setValue(parcelID+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //parcelID=id;
        //id++;
        this.status = ParcelStatus.SENT;
        this.deliveryName = "NO";
    }


//-------------------------------------//


    //--------------Ges&Set-----------------//


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

    public Location getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(Location storageLocation) {
        this.storageLocation = storageLocation;
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
                "parcelID=" + parcelID +
                ", parcelType=" + parcelType +
                ", isFragile=" + isFragile +
                ", parcelWeight=" + parcelWeight +
                ", storageLocation=" + storageLocation +
                ", deliveryParcelDate=" + deliveryParcelDate +
                ", getParcelDate=" + getParcelDate +
                ", status=" + status +
                ", deliveryName='" + deliveryName + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
