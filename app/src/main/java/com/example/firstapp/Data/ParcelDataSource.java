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
    public interface ActionKey<T>{
        void OnSuccess(T obj);
        void OnFailure(Exception exception);

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
    private static void setKeyToFireBase(final long parcelID,final ActionKey<Long> action){
        reference.child("properties/parcelId").setValue(parcelID+1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.OnSuccess(parcelID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.OnFailure(new Exception("Error to set key from server, maybe no connection to Internet!"));
            }
        });
    }
    private static void getKeyFromFireBase(final ActionKey<Long> action){
        reference.child("properties").child("parcelId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long id =dataSnapshot.getValue(int.class);
                action.OnSuccess(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                action.OnFailure(new Exception("Error to get parcel's key from server, maybe no connection to Internet!"));
            }
        });

    }
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
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    customer.setId(id);
                    for (Parcel p : customer.getParcels()) {
                        parcelList.add(p);
                    }
                    notifyParcelsDataChange.onDataChanged(parcelList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String id = dataSnapshot.getKey();
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    customer.setId(id);
                    int size=parcelList.size();
                    for (int i = 0; i <size;i++) {
                        if(parcelList.get(i).getCustomerId().equals(id)){
                            parcelList.remove(i--);
                            size--;
                        }
                    }
                    for(int i=0;i<customer.getParcels().size();i++){
                        parcelList.add(customer.getParcels().get(i));
                    }
                    notifyParcelsDataChange.onDataChanged(parcelList);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    customer.setId(id);
                    for (Parcel p : parcelList) {
                        if (p.getCustomerId() == id) {
                            parcelList.remove(p);
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
            reference.child("customers").addChildEventListener(parcelsRefChildEventListener);
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
    public static void addParcel(final Parcel parcel, final Action<Long> action) throws Exception {
        //ask to FireBase the next key of parcels can be
        getKeyFromFireBase(new ActionKey<Long>() {
            @Override
            public void OnSuccess(Long obj) {
                final String customerKey = String.valueOf(parcel.getCustomerId());
                parcel.setParcelID(obj);
                reference.child("customers").child(customerKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Customer customer=dataSnapshot.getValue(Customer.class);
                        customer.setId(customerKey);
                        action.OnProgress("update",70);

                        if(customer==null){
                            action.OnFailure(new Exception("Error, the customer don't exist!"));
                            return;
                        }

                        customer.addParcel(parcel);

                        reference.child("customers").child(customerKey).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                action.OnProgress("update",80);

                                //return to FireBase the update next key of parcels
                                setKeyToFireBase(parcel.getParcelID(),new ActionKey<Long>() {
                                    @Override
                                    public void OnSuccess(Long obj) {
                                        action.OnSuccess(parcel.getParcelID());
                                        action.OnProgress("finished",100);
                                    }

                                    @Override
                                    public void OnFailure(Exception exception) {
                                        action.OnFailure(exception);
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                action.OnFailure(e);
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
            }

            @Override
            public void OnFailure(Exception exception) {
                action.OnFailure(exception);
            }
        });
    }
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
    }
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
