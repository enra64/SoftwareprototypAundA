package de.oerntec.udprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import sp_common.DataSink;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener {
    Accelerometer mAccelerometer;
    DataSink mDataSink = null;
    ToggleButton mUdpToggle;

    EditText mPortText, mHostText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        // find our edittexts
        mHostText = (EditText) findViewById(R.id.hostText);
        mPortText = (EditText) findViewById(R.id.portText);

        // listen to the start button
        findViewById(R.id.startButton).setOnClickListener(this);

        // find the udp toggle
        mUdpToggle = (ToggleButton) findViewById(R.id.udpToggle);
    }

    @Override
    protected void onPause() {
        // super must be called
        super.onPause();

        // unregister accelerometer listener
        if (mAccelerometer != null)
            mAccelerometer.close();

        // close the socket
        if (mDataSink != null)
            mDataSink.close();
    }

    @Override
    protected void onResume() {
        // super must be called
        super.onResume();

        // initialise the sensor
        mAccelerometer = new Accelerometer(this);
    }

    private boolean createConnection() {
        // try to initialise the connection
        try {
            // udp
            if(mUdpToggle.isChecked())
                mDataSink = new UdpConnection(mHostText.getText().toString(), Integer.valueOf(mPortText.getText().toString()));
            // tdp
            else
                mDataSink = new TcpConnection(mHostText.getText().toString(), Integer.valueOf(mPortText.getText().toString()));


            // reset errors on EditTexts
            mHostText.setError(null);
            mPortText.setError(null);
        } catch (SocketException e) {
            mPortText.setError("could not bind to socket");
            e.printStackTrace();
            return false;
        } catch (UnknownHostException e) {
            mHostText.setError("could not connect to host");
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            mPortText.setError("port is not a number");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            mPortText.setError("could not find an available socket");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startButton:
                // set sensor data sink if connection was successful
                if(createConnection())
                    mAccelerometer.setDataSink(mDataSink);
                break;
        }
    }
}
