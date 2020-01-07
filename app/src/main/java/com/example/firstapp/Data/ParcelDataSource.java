package com.example.firstapp.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firstapp.Entities.Customer;
import com.example.firstapp.Entities.Parcel;
import com.example.firstapp.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;
public class ParcelDataSource {

    //----------singleton-------//
    private  ParcelDataSource(){}
    private static ParcelDataSource instance=null;

    public static ParcelDataSource getInstance() {
        if(instance==null)
            instance=new ParcelDataSource();
        return instance;
    }

    //--------interfaces-----------//
    public interface  Action<T>{
        void OnSuccess(T obj);
        void OnFailure(Exception exception);
        void OnProgress(String status, double percent);
    }
    public interface NotifyDataChange<T>{
        void  onDataChanged(T obj);
        void onFailure(Exception exception);
    }

    //----------startFields----------//
    static DatabaseReference reference;
    static List<Parcel> parcelList;
    static List<String>customerList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        parcelList = new ArrayList<>();
        customerList=new ArrayList<>();
    }
    private static ChildEventListener customersRefChildEventListener;
    private static ChildEventListener parcelsRefChildEventListener;



    //--------------methods------------------//
    public static void notifyToParcelList(final NotifyDataChange<List<Parcel>> notifyParcelsDataChange){
        if(notifyParcelsDataChange != null){
            if (parcelsRefChildEventListener != null){
                notifyParcelsDataChange.onFailure(new Exception("ERROR"));
                return;
            }
            parcelList.clear();
            parcelsRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    parcelList.add(parcel);
                    notifyParcelsDataChange.onDataChanged(parcelList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    for(int i=0;i<parcelList.size();i++){
                        if(parcel.getParcelID().equals(parcelList.get(i).getParcelID())){
                            parcelList.set(i,parcel);
                        }
                    }
                    notifyParcelsDataChange.onDataChanged(parcelList);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    for(int i=0;i<parcelList.size();i++){
                        if(parcel.getParcelID().equals(parcelList.get(i).getParcelID())){
                            parcelList.remove(i);
                            break;
                        }
                    }
                    notifyParcelsDataChange.onDataChanged(parcelList);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    notifyParcelsDataChange.onFailure(databaseError.toException());
                }
            };
            reference.child("parcels").addChildEventListener(parcelsRefChildEventListener);
        }
    }
    public static void stopNotifyToParcelList(){
        if(parcelsRefChildEventListener!=null){
            reference.removeEventListener(parcelsRefChildEventListener);
            parcelsRefChildEventListener=null;
        }
    }
    public static void notifyToCustomerList(final NotifyDataChange<List<String>> notifyCustomersDataChange){
        if(notifyCustomersDataChange != null){
            if (customersRefChildEventListener != null){
                notifyCustomersDataChange.onFailure(new Exception("ERROR"));
                return;
            }
            customerList.clear();
            customersRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    id= Utils.decodeUserEmail(id);
                    customerList.add(id);
                    notifyCustomersDataChange.onDataChanged(customerList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    notifyCustomersDataChange.onDataChanged(customerList);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    id= Utils.decodeUserEmail(id);
                    for (String strId : customerList) {
                        if (strId.equals(id)) {
                            customerList.remove(strId);
                            break;
                        }
                    }
                    notifyCustomersDataChange.onDataChanged(customerList);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    notifyCustomersDataChange.onFailure(databaseError.toException());
                }
            };
            reference.child("customers").addChildEventListener(customersRefChildEventListener);
        }
    }
    public static void stopNotifyToCustomerList(){
        if(customersRefChildEventListener!=null){
            reference.removeEventListener(customersRefChildEventListener);
            customersRefChildEventListener=null;
        }
    }
    public static void addParcel(final Parcel parcel, final Action<String> action) throws Exception {
        final String parcelId=reference.child("customers").push().getKey();
        parcel.setParcelID(parcelId);
        action.OnProgress("update",70);

        getCustomer(Utils.encodeUserEmail(parcel.getCustomerId()), new Action<Customer>() {
            @Override
            public void OnSuccess(Customer obj) {
                parcel.setAddress(obj.getAddress());

                reference.child("parcels").child(parcelId).setValue(parcel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        action.OnProgress("finished",100);
                        action.OnSuccess(parcelId);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        action.OnFailure(new Exception("Error"));
                    }
                });
            }

            @Override
            public void OnFailure(Exception exception) {
                action.OnFailure(new Exception("Error"));
            }

            @Override
            public void OnProgress(String status, double percent) {

            }
        });



    }
    /*
    public static void removeParcel(final long parcelId, final String customerId, final Action<Long> action) throws Exception {
       reference.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Customer customer=dataSnapshot.getValue(Customer.class);
               customer.setId(customerId);
               if(customer==null){
                   action.OnFailure(new Exception("The parcel don't exist maybe because the customer deleted"));
                   return;
               }
               boolean flag=false;
               for(int i=0;i<customer.getParcels().size();i++){
                   if(parcelId==customer.getParcels().get(i).getParcelID()){
                       flag=true;
                       customer.getParcels().remove(i);
                       break;
                   }
               }
               if(flag==false){
                   action.OnFailure(new Exception("The parcel don't exist!"));
                   return;
               }

               reference.child("customers").child(customerId).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       action.OnSuccess(parcelId);
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       action.OnFailure(e);
                   }
               });
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               action.OnFailure(databaseError.toException());
           }
       });
    }*/
    public static void getCustomer(final String customerId,final Action<Customer> action){
        reference.child("customers").child(customerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                String id = dataSnapshot.getKey();
                customer.setId(id);
                action.OnSuccess(customer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                action.OnFailure(databaseError.toException());
            }
        });
    }
}
