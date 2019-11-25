package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        final Package p=new Package();

        Button addButton=(Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition =  ((Spinner) findViewById(R.id.packageSpinner)).getSelectedItemPosition();
                if(itemPosition==1)
                     p.setPackageType(PackageType.ENVELOPE);
                if(itemPosition==2)
                    p.setPackageType(PackageType.SMALL_PACKAGE);
                if(itemPosition==3)
                    p.setPackageType(PackageType.BIG_PACKAGE);


                p.setFragile(findViewById(R.id.FragileCheckBox).isSelected());
                double weight =Double.parseDouble(((EditText)findViewById(R.id.weightEditText)).getText().toString());
                if(weight<=0.5)
                    p.setPackageWeight(PackageWeight.UNTIL_500_GR);
                else if(weight<=1.0)
                    p.setPackageWeight(PackageWeight.UNTIL_1_KG );
                else if(weight<=5.0)
                    p.setPackageWeight(PackageWeight.UNTIL_5_KG );
                else if(weight<=20.0)
                    p.setPackageWeight(PackageWeight.UNTIL_20_KG );

                p.setRecipientName(((EditText)findViewById(R.id.nameEditText)).getText().toString());
                p.setRecipientAddress(((EditText)findViewById(R.id.recipientAddressEditText)).getText().toString());
                p.setPhoneNumber(((EditText)findViewById(R.id.phoneEditText)).getText().toString());






            }
        });
    }

}
