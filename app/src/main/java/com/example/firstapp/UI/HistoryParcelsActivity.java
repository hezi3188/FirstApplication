package com.example.firstapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryParcelsActivity extends AppCompatActivity {

    private List<Parcel> parcelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parcelList=new ArrayList<>();
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
                String parcelId=parcelList.get(position).getParcelID();
                parcelIdTextView.setText("Details of parcel id: "+parcelId.toString());

                //customer id view
                TextView customerIdTextView=(TextView)convertView.findViewById(R.id.customerIdTextView);
                String customerId=parcelList.get(position).getCustomerId();
                customerIdTextView.setText("Recipient id: "+customerId);

                //customer date view
                TextView deliveryParcelDateTextView=(TextView)convertView.findViewById(R.id.deliveryParcelDateTextView);
                DateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy]");
                Date parcelDate=parcelList.get(position).getDeliveryParcelDate();
                deliveryParcelDateTextView.setText("Delivery date: "+dateFormat.format(parcelDate));

                //dest view
                final TextView destTextView=(TextView)convertView.findViewById(R.id.destTextView);
                ParcelDataSource.getCustomer(parcelList.get(position).getCustomerId(), new ParcelDataSource.Action<Customer>() {
                    @Override
                    public void OnSuccess(Customer obj) {
                        destTextView.setText("Target: "+obj.getAddress());
                    }

                    @Override
                    public void OnFailure(Exception exception) {

                    }

                    @Override
                    public void OnProgress(String status, double percent) {

                    }
                });

                //status view
                TextView statusTextView=(TextView)convertView.findViewById(R.id.statusTextView);
                switch (parcelList.get(position).getStatus()){
                    case SENT:statusTextView.setText("Status: Sent");break;
                    case ON_THE_WAY:statusTextView.setText("Status: On the way");break;
                    case IN_COLLECTION_PROCESS:statusTextView.setText("Status: In collection process");break;
                    case ACCEPTED:statusTextView.setText("Status: Accepted");break;
                }
                return convertView;
            }
        };
        listView.setAdapter(adapter);
        this.setContentView(listView);


        ParcelDataSource.notifyToParcelList(new ParcelDataSource.NotifyDataChange<List<Parcel>>() {
            @Override
            public void onDataChanged(List<Parcel> obj) {
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
