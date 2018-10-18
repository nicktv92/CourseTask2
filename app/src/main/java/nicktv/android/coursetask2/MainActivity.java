package nicktv.android.coursetask2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button battery, location;
    private static final int PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        battery = (Button) findViewById(R.id.button_battery);
        location = (Button) findViewById(R.id.button_location);

        battery.setOnClickListener(this);
        location.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_battery:
                toNewActivity(BatteryActivity.class);
                break;
            case R.id.button_location:
                //проверка получения разрешения от пользователя
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST);
                } else {
                    toNewActivity(LocationActivity.class);
                }
                break;
        }
    }

    //обработка нажатий в диалоговом окне запроса доступа
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toNewActivity(LocationActivity.class);
                } else {
                    Toast.makeText(this, "Необходимо разрешение", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public void toNewActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
