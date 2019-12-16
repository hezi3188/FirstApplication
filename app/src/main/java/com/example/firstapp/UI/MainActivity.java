package com.example.firstapp.UI;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.Data.ParcelDataSource;
import com.example.firstapp.Entities.Customer;
import com.example.firstapp.Entities.Parcel;
import com.example.firstapp.Entities.ParcelStatus;
import com.example.firstapp.Entities.ParcelType;
import com.example.firstapp.Entities.ParcelWeight;
import com.example.firstapp.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayAdapter<String> autoCompleteIdAdapter;
    List<String> idCustomers;

    //Declaration of variables
    Spinner parcelTypeSpinner;
    ArrayAdapter<CharSequence> parcelTypeAdapter;

    Spinner parcelWeightSpinner;
    ArrayAdapter<CharSequence> parcelWeightAdapter;

    Spinner statusSpinner;
    ArrayAdapter<CharSequence> statusAdapter;

    Button btnDatePicker;
    EditText txtDate;
    private int mYear, mMonth, mDay;


    Button btnDatePicker2;
    EditText txtDate2;
    private int mYear2, mMonth2, mDay2;


    CheckBox isFragileCheckBox;
    AutoCompleteTextView idCustomerEditText;
    EditText deliveryParcelDateEditText;
    EditText getParcelDateEditText;
    TextInputEditText deliveryNameEditText;
    ProgressBar progressBar;
    Button addParcelButton;

    LocationListener locationListener;
    LocationManager locationManager;



    public void initVariables(){
        isFragileCheckBox=(CheckBox)findViewById(R.id.isFragileCheckBox);
        idCustomerEditText=(AutoCompleteTextView)findViewById(R.id.idCustomerEditText);
        deliveryParcelDateEditText=(EditText)findViewById(R.id.deliveryParcelDateEditText);
        getParcelDateEditText=(EditText)findViewById(R.id.getParcelDateEditText);
        deliveryNameEditText=(TextInputEditText)findViewById(R.id.deliveryNameEditText);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        addParcelButton=(Button)findViewById(R.id.addParcelButton);
        parcelTypeSpinner = (Spinner)findViewById(R.id.parcelTypeSpinner);
        parcelWeightSpinner = (Spinner)findViewById(R.id.parcelWeightSpinner);
        statusSpinner = (Spinner)findViewById(R.id.statusSpinner);
    }

    public void initParcelTypeSpinner(){
        parcelTypeAdapter = ArrayAdapter.createFromResource(this,R.array.package_type_select,android.R.layout.simple_spinner_item);
        parcelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcelTypeSpinner.setAdapter(parcelTypeAdapter);
        parcelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0)
                    Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initParcelWeightSpinner(){
        parcelWeightAdapter = ArrayAdapter.createFromResource(this,R.array.package_weight_select,android.R.layout.simple_spinner_item);
        parcelWeightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcelWeightSpinner.setAdapter(parcelWeightAdapter);
        parcelWeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0)
                    Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void initStatusSpinner(){
        statusAdapter = ArrayAdapter.createFromResource(this,R.array.package_status_select,android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0)
                    Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private ParcelType getParcelType(){
        ParcelType parcelType;
        int position=parcelTypeSpinner.getSelectedItemPosition();
        switch (position){
            case 1:parcelType=ParcelType.ENVELOPE;break;
            case 2:parcelType=ParcelType.BIG_PACKAGE;break;
            case 3:parcelType=ParcelType.SMALL_PACKAGE;break;
            default:parcelType=null;
        }
        return parcelType;
    }
    private ParcelStatus getParcelStatus(){
        ParcelStatus parcelStatus;
        int position=statusSpinner.getSelectedItemPosition();
        switch (position){
            case 1:parcelStatus=ParcelStatus.SENT;break;
            case 2:parcelStatus=ParcelStatus.IN_COLLECTION_PROCESS;break;
            case 3:parcelStatus=ParcelStatus.ON_THE_WAY;break;
            case 4:parcelStatus=ParcelStatus.ACCEPTED;break;
            default:parcelStatus=null;
        }
        return parcelStatus;
    }
    private ParcelWeight getParcelWeight (){
        ParcelWeight parcelWeight;
        int position=parcelWeightSpinner.getSelectedItemPosition();
        switch (position){
            case 1:parcelWeight=ParcelWeight.UNTIL_500_GR;break;
            case 2:parcelWeight=ParcelWeight.UNTIL_1_KG;break;
            case 3:parcelWeight=ParcelWeight.UNTIL_5_KG;break;
            case 4:parcelWeight=ParcelWeight.UNTIL_20_KG;break;
            default:parcelWeight=null;
        }
        return parcelWeight;
    }
    private Parcel getParcel() throws Exception {
        final Parcel parcel=new Parcel();

        String sDate1=deliveryParcelDateEditText.getText().toString();
        Date date1=null;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
        } catch (ParseException e) {
            throw new Exception("The date of delivery error, the pattern is dd-MM-yyyy");
        }
        parcel.setDeliveryParcelDate(date1);

        String sDate2=getParcelDateEditText.getText().toString();
        Date date2=null;
        try {
            date2 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate2);
        } catch (ParseException e) {
            throw new Exception("The date of get parcel error, the pattern is dd-MM-yyyy");
        }
        parcel.setGetParcelDate(date2);
        parcel.setParcelType(getParcelType());
        parcel.setStatus(getParcelStatus());
        parcel.setParcelWeight(getParcelWeight());
        if(isFragileCheckBox.isChecked())
            parcel.setFragile(true);
        else
            parcel.setFragile(false);
        parcel.setCustomerId(idCustomerEditText.getText().toString());
        parcel.setDeliveryName(deliveryNameEditText.getText().toString());
        Location location=setLocationManager();
        getPlace(location,parcel);
        return parcel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //init spinners
        initVariables();
        initParcelTypeSpinner();
        initParcelWeightSpinner();
        initStatusSpinner();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);





        btnDatePicker=(Button)findViewById(R.id.deliveryParcelDateButton);
        txtDate=(EditText)findViewById(R.id.deliveryParcelDateEditText);
        btnDatePicker.setOnClickListener(this);


        btnDatePicker2=(Button)findViewById(R.id.getParcelDateButton);
        txtDate2=(EditText)findViewById(R.id.getParcelDateEditText);
        btnDatePicker2.setOnClickListener(this);



        addParcelButton.setOnClickListener(this);

        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        }

        idCustomers=new ArrayList<String>();
        //idCustomers.add("1");
        autoCompleteIdAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,idCustomers);
        idCustomerEditText.setAdapter(autoCompleteIdAdapter);
        ParcelDataSource.notifyToCustomerList(new ParcelDataSource.NotifyDataChange<List<String>>() {
            @Override
            public void onDataChanged(List<String> obj) {
                //idCustomers.clear();
                //idCustomers.addAll(obj);

                autoCompleteIdAdapter.clear();
                autoCompleteIdAdapter.addAll(obj);
                autoCompleteIdAdapter.notifyDataSetChanged();
                idCustomerEditText.clearFocus();

            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getBaseContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==addParcelButton){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getBaseContext(),"You must accepted GPS location",Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
                return;
            }

            Parcel p=null;
            try {
                p=getParcel();
                ParcelDataSource.addParcel(p, new ParcelDataSource.Action<Long>() {
                    @Override
                    public void OnSuccess(Long obj) {
                        Toast.makeText(getBaseContext(),"The parcel id: "+obj+ " entered",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void OnFailure(Exception exception) {
                        Toast.makeText(getBaseContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void OnProgress(String status, double percent) {
                        progressBar.setProgress((int)percent);
                    }
                });
            } catch (Exception e) {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

            Customer customer2=new Customer();
            customer2.setId("318834578");
            customer2.setFirstName("Yechezkel");
            customer2.setLastName("Ben Atar");
            customer2.setCity("Jerusalem");
            customer2.setCountry("Isratfyel");
            customer2.setBuildingNumber(23);
            customer2.setStreet("Fatal");
            customer2.setPostalAddress(45);
            customer2.setEmail("hezi@gmail.com");
            customer2.setPhoneNumber("050505050");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("customers/318834578");
            myRef.setValue(customer2);
           // idCustomers.add("1111");
            //autoCompleteIdAdapter.notifyDataSetChanged();
            //idCustomerEditText.setAdapter(autoCompleteIdAdapter);
            ///mFirebaseDatabase = FirebaseDatabase.getInstance();
            //mMessageDatabaseReference = mFirebaseDatabase.getReference().child("customers");

            //  mMessageDatabaseReference.setValue(customers);
/*
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("customers/0/parcels/65");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Parcel val=dataSnapshot.getValue(Parcel.class);
                    Toast.makeText(getBaseContext(),val.toString(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
*/
        }
        // for deliveryParcelDate
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }


        //for getParcelDate
        if (v == btnDatePicker2) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear2 = c.get(Calendar.YEAR);
            mMonth2 = c.get(Calendar.MONTH);
            mDay2 = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear2, mMonth2, mDay2);
            datePickerDialog.show();
        }
       }



    private Location setLocationManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return null;// locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else {
            // Android version is lesser than 6.0 or the permission is already granted.
           // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public void getPlace(Location location,Parcel parcel) throws Exception  {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                parcel.setLongitude(addresses.get(0).getLongitude());
                parcel.setLatitude(addresses.get(0).getLatitude());
                return ;
            }
            throw new  Exception("no location");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        throw new Exception("Error to get location!");
    }


    @Override
    protected void onDestroy() {
        ParcelDataSource.stopNotifyToCustomerList();
        super.onDestroy();
    }
}



//private FirebaseDatabase mFirebaseDatabase;
//private DatabaseReference mMessageDatabaseReference;
//Parcel parcel;
//List<Customer> customers = new ArrayList<Customer>();

//ParcelDataSource parcelDataSource=ParcelDataSource.getInstance();













/*
        Parcel parcel=new Parcel();
        parcel.setParcelType(ParcelType.BIG_PACKAGE);
        parcel.setFragile(true);
        parcel.setParcelWeight(ParcelWeight.UNTIL_5_KG);
        parcel.setLatitude(545588.454);
        parcel.setLongitude(5459.4524);
        parcel.setDeliveryParcelDate(new Date(8,4,7));
        parcel.setGetParcelDate(new Date(4,5,4));
        parcel.setStatus(ParcelStatus.IN_COLLECTION_PROCESS);
        parcel.setCustomerId("0");
        parcel.setDeliveryName("mosגגדכגכhe");
        try {
            ParcelDataSource.addParcel(parcel, new ParcelDataSource.Action<Long>() {
                @Override
                public void OnSuccess(Long obj) {
                    Toast.makeText(getBaseContext(),"dsdsebhbkbhjhjghjbghjlected",Toast.LENGTH_LONG).show();
                }

                @Override
                public void OnFailure(Exception exception) {
                    Toast.makeText(getBaseContext(),"dsdsקקקקקקקקקקקקקקd",Toast.LENGTH_LONG).show();

                }

                @Override
                public void OnProgress(String status, double percent) {

                }
            });
        }
        catch (Exception e){

        }
*/








 /*
/*try {


    ParcelDataSource.removeParcel(0, "0", new ParcelDataSource.Action<Long>() {
        @Override
        public void OnSuccess(Long obj) {

        }

        @Override
        public void OnFailure(Exception exception) {

        }

        @Override
        public void OnProgress(String status, double percent) {

        }
    });
}
catch (Exception e){

}





        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        Customer customer3 = new Customer();
        customer1.setId("203342696");
        customer1.setFirstName("Yigal");
        customer1.setLastName("Fischler");
        customer1.setCity("Jerusalem");
        customer1.setCountry("Israel");
        customer1.setBuildingNumber(23);
        customesr1.setStreet("Golomb");
        customer1.setPostalAddress(45243);
        customer1.etEmail("yigalf93@gmail.com");
        customer1.setPhoneNumber("0548002508");

        customer2.setId("318834579");
        customer2.setFirstName("Yechezkel");
        customer2.setLastName("Ben Atar");
        customer2.setCity("Jerusalem");
        customer2.setCountry("Isratfyel");
        customer2.setBuildingNumber(23);
        customer2.setStreet("Fatal");
        customer2.setPostalAddress(45);
        customer2.setEmail("hezi@gmail.com");
        customer2.setPhoneNumber("050505050");
        customers.add(customer1);
        customers.add(customer2);


        ///mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mMessageDatabaseReference = mFirebaseDatabase.getReference().child("customers");

      //  mMessageDatabaseReference.setValue(customers);


        final Parcel p=new Parcel();


              //  p.setRecipientName(((EditText)findViewById(R.id.nameEditText)).getText().toString());
              //  p.setRecipientAddress(((EditText)findViewById(R.id.recipientAddressEditText)).getText().toString());
              //  p.setPhoneNumber(((EditText)findViewById(R.id.phoneEditText)).getText().toString());






            }
        });
    */
