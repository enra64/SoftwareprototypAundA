package de.oerntec.udprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.SocketException;
import java.net.UnknownHostException;

public class ConnectActivity extends AppCompatActivity {
    Accelerometer mAccelerometer;
    DataConnection mConnection = null;

    EditText mPortText, mHostText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mHostText = (EditText) findViewById(R.id.hostText);
        mPortText = (EditText) findViewById(R.id.portText);
    }

    @Override
    protected void onPause() {
        // super must be called
        super.onPause();

        // unregister accelerometer listener
        mAccelerometer.close();

        // close the socket
        mConnection.close();
    }

    @Override
    protected void onResume() {
        // super must be called
        super.onResume();

        // try initialise the connection
        try {
            mConnection = new DataConnection(
                    mHostText.getText().toString(),
                    Integer.valueOf(mPortText.getText().toString()));

            // reset errors on edittexts
            mHostText.setError(null);
            mPortText.setError(null);
        } catch (SocketException e){
            mPortText.setError("could not bind to socket");
        } catch (UnknownHostException e){
            mHostText.setError("could not connect to host");
        }

        // initialise the sensor
        mAccelerometer = new Accelerometer(this);
    }

    /**
     * Called when the start button is pressed
     */
    void onStartClick(View v){
        mAccelerometer.setDataSink(mConnection);
    }
}
