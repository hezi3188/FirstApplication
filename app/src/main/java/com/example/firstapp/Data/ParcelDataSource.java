package com.example.firstapp.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firstapp.Entities.Customer;
import com.example.firstapp.Entities.Parcel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    static List<Customer>customerList;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ParcelRef = database.getReference("customers");
        parcelList = new ArrayList<>();
        customerList=new ArrayList<>();
    }

    private static ChildEventListener parcelsRefChildEventListener;
    private static ChildEventListener customersRefChildEventListener;

    public static void notifyToParcelList(final NotifyDataChange<List<Parcel>> notifyDataChange){}
    public static void stopNotifyToParcelList(){}

    public static void notifyToCustomerAndParcelList(final NotifyDataChange<List<Customer>> notifyCustomersDataChange, final NotifyDataChange<List<Parcel>> notifyParcelsDataChange){
        if(notifyCustomersDataChange != null){
            if (customersRefChildEventListener != null){
                notifyCustomersDataChange.onFailure(new Exception("ERROR"));
            }
            customerList.clear();
            customersRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    customer.setId(id);
                    customerList.add(customer);
                    for (Parcel p : customer.getParcels()) {
                        parcelList.add(p);
                    }
                    notifyCustomersDataChange.onDataChanged(customerList);
                    notifyParcelsDataChange.onDataChanged(parcelList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    customer.setId(id);
                    for (int i = 0; i < customerList.size(); ++i) {
                        if (customerList.get(i).getId().equals(id)) {
                            customerList.set(i, customer);
                            for (Parcel p : parcelList) {
                                if (p.getCustomerId() == id) {
                                    parcelList.remove(p);
                                }
                            }
                            for (Parcel p : customer.getParcels()) {
                                parcelList.add(p);
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    customer.setId(id);
                    for (Customer c : customerList) {
                        if (c.getId().equals(id)) {
                            customerList.remove(c);
                            break;
                        }
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    notifyCustomersDataChange.onFailure(databaseError.toException());

                }
            };
        }
    }
    public static void stopNotifyToCustomrsList(){}


    private static void addParcelToFireBase(final Parcel parcel, final Action<Long> action) throws Exception {
        String customerKey = String.valueOf(parcel.getCustomerId());
        Customer customer;
        customer=null;
        for (int i=0;i<customerList.size();i++){
            if(customerList.get(i).getId().equals(customerKey)){
                customer=customerList.get(i);
                break;
            }
        }
        if(customer==null)
            throw new Exception("The customer id: "+customerKey+" don't exist");


        customer.addParcel(parcel);
        ParcelRef.child(customerKey).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            action.OnSuccess(parcel.getParcelID());
            action.OnProgress("upload parcel data", 100);
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
