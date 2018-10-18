package nicktv.android.coursetask2;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    TextView mGPSStatus, mNetworkStatus, mGPSLocation, mNetworkLocation;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mGPSStatus = (TextView) findViewById(R.id.gpsTitle);
        mNetworkStatus = (TextView) findViewById(R.id.networkTitle);
        mGPSLocation = (TextView) findViewById(R.id.gpsLocation);
        mNetworkLocation = (TextView) findViewById(R.id.networkLocation);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
            //регистрирация получения обновлеий данных о местоположении используя данные сети и GPS
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        checkEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
            //отмена регистрации получения обновлений данных о местоположении
        locationManager.removeUpdates(this);
    }

        //реализация методов интерфеса LocationListener
    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onProviderEnabled(String provider) {
        checkEnabled();
        showLocation(locationManager.getLastKnownLocation(provider));
    }

    @Override
    public void onProviderDisabled(String provider) {
        checkEnabled();
    }

        //вывод полученных данных в TextView
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            mGPSLocation.setText(formatLocation(location));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            mNetworkLocation.setText(formatLocation(location));
        }
    }

        //формирование строки String для вывода  в методе showLocation();
    private String formatLocation(Location location) {
        if (location == null) return "Нет данных о местоположении";

        String address = "";

        try {
            //получение адреса по полученным данным о местоположении
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses;
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
            } else {
                address = "Широта " + location.getLatitude() + "\n" +
                        "Долгота " + location.getLongitude() + "\n" +
                        "Высота " + location.getAltitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    //проверка доступа к провайдерам
    private void checkEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mGPSStatus.setTextColor(getResources().getColor(R.color.colorEnable));
        } else {
            mGPSStatus.setTextColor(getResources().getColor(R.color.colorDisable));
            mGPSLocation.setText("");
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            mNetworkStatus.setTextColor(getResources().getColor(R.color.colorEnable));
        } else {
            mNetworkStatus.setTextColor(getResources().getColor(R.color.colorDisable));
            mNetworkLocation.setText("");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}