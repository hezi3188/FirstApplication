package com.example.firstapp.UI;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.firstapp.Entities.Parcel;
import com.example.firstapp.Entities.ParcelType;
import com.example.firstapp.Entities.ParcelWeight;
import com.example.firstapp.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    //Declaration of views
    CheckBox isFragileCheckBox;
    AutoCompleteTextView idCustomerEditText;
    EditText deliveryParcelDateEditText;
    ProgressBar progressBar;
    Button addParcelButton;
    Spinner parcelTypeSpinner;
    Spinner parcelWeightSpinner;
    Button btnDatePicker;
    EditText txtDate;

    //Declaration of adapters
    ArrayAdapter<CharSequence> parcelTypeAdapter;
    ArrayAdapter<CharSequence> parcelWeightAdapter;
    ArrayAdapter<String> autoCompleteIdAdapter;

    //Declaration of locations
    LocationListener locationListener;
    LocationManager locationManager;

    //Declaration of auxiliary variables
    List<String> idCustomers;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialization
        findViews();
        initParcelTypeSpinner();
        initParcelWeightSpinner();
        initIdCustomersAuto();

        //on click declarations
        btnDatePicker.setOnClickListener(this);
        addParcelButton.setOnClickListener(this);

        //check GPS permission
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
       //     requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
       // }

        //init location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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
    }

    @Override
    protected void onDestroy() {
        ParcelDataSource.stopNotifyToCustomerList();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.parcel_add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.historyParcels:
                Intent intent=new Intent(MainActivity.this,HistoryParcelsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        //for add parcel
        if(v==addParcelButton){
            if(!checkViews()){
                return;
            }

            addParcel();
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
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        }
    }

    private void resetView(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(0);
                addParcelButton.setEnabled(true);
                idCustomerEditText.setEnabled(true);
                parcelTypeSpinner.setEnabled(true);
                parcelWeightSpinner.setEnabled(true);
                isFragileCheckBox.setEnabled(false);
                deliveryParcelDateEditText.setText("");
                idCustomerEditText.setText("");

            }
        },1500);
    }
    private void addParcel(){
        //check location access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(),"You must accepted GPS location",Toast.LENGTH_LONG).show();
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return;
        }

        Parcel p=null;
        try {
            p=getParcel();
            addParcelButton.setEnabled(false);
            idCustomerEditText.setEnabled(false);
            parcelTypeSpinner.setEnabled(false);
            parcelWeightSpinner.setEnabled(false);
            isFragileCheckBox.setEnabled(false);
            ParcelDataSource.addParcel(p, new ParcelDataSource.Action<String>() {
                @Override
                public void OnSuccess(String obj) {
                    Toast.makeText(getBaseContext(),"The parcel id: "+obj+ " entered",Toast.LENGTH_LONG).show();
                    resetView();
                }

                @Override
                public void OnFailure(Exception exception) {
                    Toast.makeText(getBaseContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                    resetView();
                }

                @Override
                public void OnProgress(String status, double percent) {
                    progressBar.setProgress((int)percent);
                    addParcelButton.setEnabled(false);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            resetView();
        }

    }
    private boolean checkViews(){
        boolean checkIdCustomer=idCustomerEditText.getText().toString().isEmpty();
        boolean checkDeliveryParcelDate=deliveryParcelDateEditText.getText().toString().isEmpty();
        boolean checkParcelType=parcelTypeSpinner.getSelectedItemPosition()==0;
        boolean checkWeightPackage=parcelWeightSpinner.getSelectedItemPosition()==0;
        if(!idCustomers.contains(idCustomerEditText.getText().toString())&&!checkIdCustomer){
            Toast.makeText(getBaseContext(),"Error, the id is not member friend",Toast.LENGTH_LONG).show();
            return  false;
        }
        if( !checkIdCustomer&&!checkDeliveryParcelDate&&!
                checkParcelType&&!checkWeightPackage){
            return true;
        }
        else {
            Toast.makeText(getBaseContext(),"Fill all the details please!",Toast.LENGTH_LONG).show();
            return false;
        }
    }
    private void findViews(){
        isFragileCheckBox=(CheckBox)findViewById(R.id.isFragileCheckBox);
        idCustomerEditText=(AutoCompleteTextView)findViewById(R.id.idCustomerEditText);
        deliveryParcelDateEditText=(EditText)findViewById(R.id.deliveryParcelDateEditText);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        addParcelButton=(Button)findViewById(R.id.addParcelButton);
        parcelTypeSpinner = (Spinner)findViewById(R.id.parcelTypeSpinner);
        parcelWeightSpinner = (Spinner)findViewById(R.id.parcelWeightSpinner);
        btnDatePicker=(Button)findViewById(R.id.deliveryParcelDateButton);
        txtDate=(EditText)findViewById(R.id.deliveryParcelDateEditText);
    }
    private void initParcelTypeSpinner(){
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
    private void initParcelWeightSpinner(){
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
    private void initIdCustomersAuto(){
        idCustomers=new ArrayList<String>();
        autoCompleteIdAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,idCustomers);
        idCustomerEditText.setAdapter(autoCompleteIdAdapter);
        ParcelDataSource.notifyToCustomerList(new ParcelDataSource.NotifyDataChange<List<String>>() {
            @Override
            public void onDataChanged(List<String> obj) {
                idCustomers=obj;
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

        //set delivery parcel date
        String sDate1=deliveryParcelDateEditText.getText().toString();
        Date date1=null;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
        } catch (ParseException e) {
            throw new Exception("The date of delivery error, the pattern is dd-MM-yyyy");
        }
        parcel.setDeliveryParcelDate(date1);



            //set location
        Location location= getLocation();
        setLocation(location,parcel);

        //set everything else
        parcel.setParcelType(getParcelType());
        parcel.setParcelWeight(getParcelWeight());
        if(isFragileCheckBox.isChecked())
            parcel.setFragile(true);
        else
            parcel.setFragile(false);
        parcel.setCustomerId(idCustomerEditText.getText().toString());
        return parcel;
    }
    private Location getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return null;
        }
        else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addParcel();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setLocation(Location location, Parcel parcel) throws Exception  {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                parcel.setLongitude(addresses.get(0).getLongitude());
                parcel.setLatitude(addresses.get(0).getLatitude());
                return ;
            }
            throw new  Exception("Error, no location");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        throw new Exception("Error to get location!");
    }
}