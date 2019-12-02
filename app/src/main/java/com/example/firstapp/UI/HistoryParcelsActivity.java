package com.example.firstapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.firstapp.Entities.*;
import com.example.firstapp.Entities.Parcel;
import com.example.firstapp.Entities.ParcelType;
import com.example.firstapp.Entities.ParcelWeight;
import com.example.firstapp.R;

public class HistoryParcelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_parcel);
    }
}
