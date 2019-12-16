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

    //----------startFields----------//
    static DatabaseReference ParcelRef;
    static List<Parcel> parcelList;
    static List<String>customerList;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ParcelRef = database.getReference("customers");
        parcelList = new ArrayList<>();
        customerList=new ArrayList<>();
    }
    private static ChildEventListener customersRefChildEventListener;
    private static ChildEventListener parcelsRefChildEventListener;



    //--------------methods------------------//
    public static void notifyToCustomerAndParcelList(final NotifyDataChange<List<Customer>> notifyCustomersDataChange, final NotifyDataChange<List<Parcel>> notifyParcelsDataChange){
        if(notifyCustomersDataChange != null){//need to check
            if (customersRefChildEventListener != null){
                notifyCustomersDataChange.onFailure(new Exception("ERROR"));
                notifyParcelsDataChange.onFailure(new Exception("ERROR"));
                return;
            }
            //we do clear because in first time onChildAdded add all the customers
            customerList.clear();
            parcelList.clear();
            customersRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    customer.setId(id);
                    customerList.add(customer.toString());
                    for (Parcel p : customer.getParcels()) {
                        parcelList.add(p);
                    }
                    //notifyCustomersDataChange.onDataChanged();
                    notifyParcelsDataChange.onDataChanged(parcelList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    String id = dataSnapshot.getKey();
                    customer.setId(id);
                    /*for (int i = 0; i < customerList.size(); ++i) {
                        if (customerList.get(i).getId().equals(id)) {
                            customerList.set(i, customer);
                            for (Parcel p : parcelList) {//here we can get bombastic changes at the list
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

                    notifyCustomersDataChange.onDataChanged(customerList);*/
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
                   /* for (Customer c : customerList) {//maybe do the for such as OSF
                        if (c.getId().equals(id)) {
                            customerList.remove(c);
                            break;
                        }
                    }*/

                   // notifyCustomersDataChange.onDataChanged(customerList);
                    notifyParcelsDataChange.onDataChanged(parcelList);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    notifyCustomersDataChange.onFailure(databaseError.toException());
                    notifyParcelsDataChange.onFailure(databaseError.toException());
                }
            };
            ParcelRef.addChildEventListener(customersRefChildEventListener);
        }
    }
    public static void stopNotifyToCustomerAndParcelList(){
        if(customersRefChildEventListener!=null){
            ParcelRef.removeEventListener(customersRefChildEventListener);
            customersRefChildEventListener=null;
        }
    }

    public static void notifyToCustomerList(final NotifyDataChange<List<String>> notifyCustomersDataChange){
        if(notifyCustomersDataChange != null){//need to check
            if (customersRefChildEventListener != null){
                notifyCustomersDataChange.onFailure(new Exception("ERROR"));
                return;
            }
            //we do clear because in first time onChildAdded add all the customers
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

                    for (String strId : customerList) {//maybe do the for such as OSF
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
            ParcelRef.addChildEventListener(customersRefChildEventListener);
        }
    }

    public static void stopNotifyToCustomerList(){
        if(customersRefChildEventListener!=null){
            ParcelRef.removeEventListener(customersRefChildEventListener);
            customersRefChildEventListener=null;
        }
    }




    public static void addParcel(final Parcel parcel, final Action<Long> action) throws Exception {
        parcel.getKeyFromFireBase(new Parcel.Action<Long>() {
            @Override
            public void OnSuccess(Long obj) {
                final String customerKey = String.valueOf(parcel.getCustomerId());
                parcel.setParcelID(obj);
                ParcelRef.child(customerKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Customer customer=dataSnapshot.getValue(Customer.class);
                        customer.setId(customerKey);
                        action.OnProgress("update",70);

                        if(customer==null){
                            action.OnFailure(new Exception("The customer don't exist!"));
                            return;
                        }

                        customer.addParcel(parcel);

                        ParcelRef.child(customerKey).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                action.OnProgress("update",80);
                                parcel.setKeyToFireBase(new Parcel.Action<Long>() {
                                    @Override
                                    public void OnSuccess(Long obj) {
                                        action.OnSuccess(parcel.getParcelID());
                                        action.OnProgress("finished",100);
                                    }

                                    @Override
                                    public void OnFailure(Exception exception) {

                                    }
                                });

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

            }


        });
    }


    public static void removeParcel(final long parcelId, final String customerId, final Action<Long> action) throws Exception {
       ParcelRef.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
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

               ParcelRef.child(customerId).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}
