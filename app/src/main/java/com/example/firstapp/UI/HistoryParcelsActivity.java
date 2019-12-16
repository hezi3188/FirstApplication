package com.example.firstapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstapp.Data.ParcelDataSource;
import com.example.firstapp.Entities.*;
import com.example.firstapp.Entities.Parcel;
import com.example.firstapp.Entities.ParcelType;
import com.example.firstapp.Entities.ParcelWeight;
import com.example.firstapp.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryParcelsActivity extends AppCompatActivity {

    private List<Parcel> parcelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parcelList=new ArrayList<>();
        Parcel p=new Parcel();
        p.setCustomerId("77");
        p.setParcelID(978);
        parcelList.add(p);
        parcelList.add(p);
        parcelList.add(p);
        //setContentView(R.layout.activity_history_parcel);
        initItemByListView();

        /*ParcelDataSource.notifyToParcelList(new ParcelDataSource.NotifyDataChange<List<Parcel>>() {
            @Override
            public void onDataChanged(List<Parcel> obj) {
                parcelList=obj;
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });*/

    }

    @Override
    protected void onDestroy() {
        ParcelDataSource.stopNotifyToParcelList();
        super.onDestroy();
    }

    void initItemByListView() {
        ListView listView=new ListView(this);
        ArrayAdapter<Parcel> adapter = new ArrayAdapter<Parcel>(this,R.layout.adapter_parcel_item, parcelList)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                if(convertView==null){
                    convertView=View.inflate(HistoryParcelsActivity.this,R.layout.adapter_parcel_item,null);
                }
                TextView parcelIdTextView=(TextView)findViewById(R.id.parcelIdTextView);
                //parcelIdTextView.setText("56");
                //parcelIdTextView.setText(((Long)parcelList.get(position).getParcelID()).toString());
                return convertView;
            }
        };
        listView.setAdapter(adapter);
        this.setContentView(listView);
    }
}
