package com.example.firstapp.Data;

import androidx.annotation.NonNull;

import com.example.firstapp.Entities.Parcel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.*;
public class ParcelDataSource {
    public interface  Action<T>{
        void OnSuccess(T obj);
        void OnFailure(Exception exception);
        void OnProgress(String status, double percent);
    }
    public  interface NotifyDataChange<T>{
        void  onDataChanged(T obj);
        void onFailure(Exception exception);
    }
    static  DatabaseReference ParcelRef;
    static List<Parcel> parcelList;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ParcelRef = database.getReference();
        parcelList = new ArrayList<>();
    }
    private static void addStudentToFirebase(final Parcel parcel, final Action<Long> action) {
        String key = String.valueOf(parcel.getParcelID());
    ParcelRef.child(key).setValue(parcel).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            action.OnSuccess(parcel.getParcelID());
            action.OnProgress("upload student data", 100);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            action.OnFailure(e);
            action.OnProgress("error upload parcel data", 100);
        }
    });
    }
    private static class SingletoneHolder{
        private final static ParcelDataSource instance = new ParcelDataSource();
    }
    public static ParcelDataSource getInstance(){
        return  SingletoneHolder.instance;
    }
    public static void addParcel(final Parcel parcel, final Action<Long> action){

    }





}
