package de.oerntec.udprototype;

import android.os.AsyncTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;

import sp_common.DataSink;
import sp_common.SensorData;

/**
 * Created by arne on 10/17/16.
 */

public class DataConnection implements DataSink {
    private DatagramSocket mSocket;
    private InetAddress mHost;
    private int mPort;

    /**
     * Initialize the connection using specified port
     */
    DataConnection(String host, int port) throws IOException {
        // save host(translated) and port
        mPort = port;
        mHost = InetAddress.getByName(host);

        // create socket from host and port
        DatagramChannel channel = DatagramChannel.open();
        mSocket = channel.socket();
    }

    /**
     * call before exiting
     */
    @Override
    public void close(){
        mSocket.close();
    }

    /**
     * Push new data to the remote partner
     */
    @Override
    public void onData(SensorData sensorData) {
        new SensorOut().execute(sensorData);
    }

    private class SensorOut extends AsyncTask<SensorData, Void, Boolean> {

        @Override
        protected Boolean doInBackground(SensorData... sensorData) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(sensorData[0]);
                byte[] data = outputStream.toByteArray();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, mHost, mPort);
                mSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
    }
}
