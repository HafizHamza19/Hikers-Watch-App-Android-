package com.example.hafizhamza.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
LocationManager locationManager;
    LocationListener locationListener;
    TextView Lattitude;
    TextView Longtitude;
    TextView Accuracy;
    TextView Alttude;
    TextView Address;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                StartListening();
            }
        }
    }
public  void StartListening()
{
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
             UpdateLocation(location);
              //  Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT<23)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Location LastKnownLoction=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (LastKnownLoction!=null)
                {
                UpdateLocation(LastKnownLoction);
                }
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }
    public void UpdateLocation(Location location)
    {
        Lattitude=(TextView)findViewById(R.id.MyLattitude);
        Longtitude=(TextView)findViewById(R.id.MyLongtitude);
         Accuracy=(TextView) findViewById(R.id.MyAccuracy);
         Alttude=(TextView)findViewById(R.id.MyAltitude);
         Address=(TextView)findViewById(R.id.MyAddress);
        Longtitude.setText("Longtitude : "+Double.toString(location.getLongitude()));
        Lattitude.setText("Lattitude : "+Double.toString(location.getLatitude()));
         Accuracy.setText("Accuracy : "+Double.toString(location.getAccuracy()));
         Alttude.setText("ALtitude : "+Double.toString(location.getAltitude()));
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String address="Address Is Not Found :(";
        try {

            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (addressList.get(0)!=null && addressList.size()>0)
            {
                address="Address : ";
                  if (addressList.get(0).getSubThoroughfare()!=null)
                  {
                        address+=addressList.get(0).getSubThoroughfare()+"\n";
                  }
                if (addressList.get(0).getLocality()!=null)
                {
                    address+=addressList.get(0).getLocality()+" ";
                }
                if (addressList.get(0).getPostalCode()!=null)
                {
                    address+=addressList.get(0).getPostalCode()+" ";
                }
                if (addressList.get(0).getAdminArea()!=null)
                {
                    address+=addressList.get(0).getAdminArea();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Address.setText(address);
    }
}
