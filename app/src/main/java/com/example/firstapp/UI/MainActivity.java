package com.example.firstapp.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.Entities.Parcel;
import com.example.firstapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
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

    Parcel parcel;

    //private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mMessageDatabaseReference;
    //Parcel parcel;
    //List<Customer> customers = new ArrayList<Customer>();

    //ParcelDataSource parcelDataSource=ParcelDataSource.getInstance();



    public void initParcelTypeSpinner(){
        parcelTypeSpinner = (Spinner)findViewById(R.id.parcelTypeSpinner);
        parcelTypeAdapter = ArrayAdapter.createFromResource(this,R.array.package_type_select,android.R.layout.simple_spinner_item);
        parcelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcelTypeSpinner.setAdapter(parcelTypeAdapter);
        parcelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initParcelWeightSpinner(){
        parcelWeightSpinner = (Spinner)findViewById(R.id.parcelWeightSpinner);
        parcelWeightAdapter = ArrayAdapter.createFromResource(this,R.array.package_weight_select,android.R.layout.simple_spinner_item);
        parcelWeightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcelWeightSpinner.setAdapter(parcelWeightAdapter);
        parcelWeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void initStatusSpinner(){
        statusSpinner = (Spinner)findViewById(R.id.statusSpinner);
        statusAdapter = ArrayAdapter.createFromResource(this,R.array.package_status_select,android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init spinners
        initParcelTypeSpinner();
        initParcelWeightSpinner();
        initStatusSpinner();







        btnDatePicker=(Button)findViewById(R.id.deliveryParcelDateButton);
        txtDate=(EditText)findViewById(R.id.deliveryParcelDateEditText);
        btnDatePicker.setOnClickListener(this);


        btnDatePicker2=(Button)findViewById(R.id.getParcelDateButton);
        txtDate2=(EditText)findViewById(R.id.getParcelDateEditText);
        btnDatePicker2.setOnClickListener(this);
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


        Parcel parcel=new Parcel();
        parcel.setParcelType(ParcelType.BIG_PACKAGE);
        parcel.setFragile(true);
        parcel.setParcelWeight(ParcelWeight.UNTIL_5_KG);
        parcel.setStorageLocation(new Location("hi"));
        parcel.setDeliveryParcelDate(new Date(4,8,4));
        parcel.setGetParcelDate(new Date(7,8,7));
        parcel.setStatus(ParcelStatus.IN_COLLECTION_PROCESS);
        parcel.setCustomerId("1");
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



        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        Customer customer3 = new Customer();
        customer1.setId("203342696");
        customer1.setFirstName("Yigal");
        customer1.setLastName("Fischler");
        customer1.setCity("Jerusalem");
        customer1.setCountry("Israel");
        customer1.setBuildingNumber(23);
        customer1.setStreet("Golomb");
        customer1.setPostalAddress(45243);
        customer1.setEmail("yigalf93@gmail.com");
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

        Button addButton=(Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition =  ((Spinner) findViewById(R.id.packageSpinner)).getSelectedItemPosition();
                if(itemPosition==1)
                     p.setParcelType(ParcelType.ENVELOPE);
                if(itemPosition==2)
                    p.setParcelType(ParcelType.SMALL_PACKAGE);
                if(itemPosition==3)
                    p.setParcelType(ParcelType.BIG_PACKAGE);


                p.setFragile(findViewById(R.id.FragileCheckBox).isSelected());
                double weight =Double.parseDouble(((EditText)findViewById(R.id.weightEditText)).getText().toString());
                if(weight<=0.5)
                    p.setParcelWeight(ParcelWeight.UNTIL_500_GR);
                else if(weight<=1.0)
                    p.setParcelWeight(ParcelWeight.UNTIL_1_KG );
                else if(weight<=5.0)
                    p.setParcelWeight(ParcelWeight.UNTIL_5_KG );
                else if(weight<=20.0)
                    p.setParcelWeight(ParcelWeight.UNTIL_20_KG );

              //  p.setRecipientName(((EditText)findViewById(R.id.nameEditText)).getText().toString());
              //  p.setRecipientAddress(((EditText)findViewById(R.id.recipientAddressEditText)).getText().toString());
              //  p.setPhoneNumber(((EditText)findViewById(R.id.phoneEditText)).getText().toString());






            }
        });
    */


}
    @Override
    public void onClick(View v) {
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
}
