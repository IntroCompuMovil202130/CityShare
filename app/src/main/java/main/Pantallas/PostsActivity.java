package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class PostsActivity extends AppCompatActivity  implements SensorEventListener {

    private SensorManager mgr;
    private Sensor temp;
    private TextView text;
    private StringBuilder msg = new StringBuilder(2048);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        temp = mgr.getDefaultSensor( Sensor.TYPE_AMBIENT_TEMPERATURE);
        text = (TextView) findViewById(R.id.text);

    }
    @Override
    protected void onResume() {
        mgr.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();
    }
    @Override
    protected void onPause() {
        mgr.unregisterListener(this, temp);
        super.onPause();
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        text.setText(" ");
        msg.insert(0, event.values[0] + " °C");
        text.setText(msg);
        text.invalidate();
    }
}