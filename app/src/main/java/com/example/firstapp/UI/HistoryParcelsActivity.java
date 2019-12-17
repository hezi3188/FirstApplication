package com.example.firstapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryParcelsActivity extends AppCompatActivity {

    private List<Parcel> parcelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_history_parcel);
        parcelList=new ArrayList<>();
        /*Parcel p=new Parcel();
        p.setCustomerId("77");
        p.setParcelID(978);
        p.setDeliveryParcelDate(new Date(8,1,1));
        parcelList.add(p);
        parcelList.add(p);
        parcelList.add(p);
        parcelList.add(p);
        parcelList.add(p);
        parcelList.add(p);*/
        //parcelList.add(p);
        //parcelList.add(p);
        //parcelList.add(p);
        //parcelList.add(p);
        //parcelList.add(p);
        //parcelList.add(p);
        //parcelList.add(p);
        //parcelList.add(p);
        //parcelList.add(p);
        //parcelIdTextVieww=(TextView)findViewById(R.id.parcelIdTextVieww);
        //parcelIdTextVieww.setText("56");
        ListView listView=new ListView(this);
        final ArrayAdapter<Parcel> adapter = new ArrayAdapter<Parcel>(this,R.layout.adapter_parcel_item, parcelList)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                if(convertView==null){
                    convertView=View.inflate(HistoryParcelsActivity.this,R.layout.adapter_parcel_item,null);
                }
                //parcel id view
                TextView parcelIdTextView=(TextView)convertView.findViewById(R.id.parcelIdTextView);
                Long parcelId=parcelList.get(position).getParcelID();
                parcelIdTextView.setText("The parcel id: "+parcelId.toString());

                //customer id view
                TextView customerIdTextView=(TextView)convertView.findViewById(R.id.customerIdTextView);
                String customerId=parcelList.get(position).getCustomerId();
                customerIdTextView.setText("The customer id: "+customerId);

                //customer date view
                TextView deliveryParcelDateTextView=(TextView)convertView.findViewById(R.id.deliveryParcelDateTextView);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
                Date parcelDate=parcelList.get(position).getDeliveryParcelDate();
                deliveryParcelDateTextView.setText("The delivary date: "+dateFormat.format(parcelDate));



                return convertView;
            }
        };
        listView.setAdapter(adapter);
        this.setContentView(listView);


        ParcelDataSource.notifyToParcelList(new ParcelDataSource.NotifyDataChange<List<Parcel>>() {
            @Override
            public void onDataChanged(List<Parcel> obj) {
                //parcelList=obj;
                adapter.clear();
                adapter.addAll(obj);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getBaseContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        ParcelDataSource.stopNotifyToParcelList();
        super.onDestroy();
    }


}
