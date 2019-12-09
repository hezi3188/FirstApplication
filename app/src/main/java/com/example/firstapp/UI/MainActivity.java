package com.example.firstapp.UI;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.Entities.Customer;
import com.example.firstapp.Entities.Parcel;
import com.example.firstapp.Entities.ParcelType;
import com.example.firstapp.Entities.ParcelWeight;
import com.example.firstapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;

    List<Customer> customers = new ArrayList<Customer>();


    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  Customer customer1 = new Customer();
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
        customer2.setCountry("Israel");
        customer2.setBuildingNumber(23);
        customer2.setStreet("Fatal");
        customer2.setPostalAddress(45);
        customer2.setEmail("hezi@gmail.com");
        customer2.setPhoneNumber("050505050");
        customers.add(customer1);
        customers.add(customer2);
*/

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("customers");

        mMessageDatabaseReference.setValue(customers);
        spinner = (Spinner)findViewById(R.id.packageSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.package_type_select,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+"selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    }

}
