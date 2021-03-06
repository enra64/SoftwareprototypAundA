package de.oerntec.udprototype;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.channels.DatagramChannel;
import java.util.Date;

import sp_common.DataSink;
import sp_common.SensorData;

/**
 * The UdpConnection class is an UDP implementation of the DataSink interface; it sends all input
 * data to the host and port given in the constructor
 */

class UdpConnection implements DataSink {
    /**
     * The socket we use for sending data; initialized in the constructor
     */
    private DatagramSocket mSocket;

    /**
     * The target host for our UDP packets
     */
    private InetAddress mHost;

    /**
     * The target port on {@link #mHost} for our UDP packets
     */
    private final int mPort;

    /**
     * Variable denoting whether we already have written out our initial sensor/system timestamp pair
     */
    private boolean mInitializationSent = false;

    /**
     * The temporal distance between the sensor timestamps and the system time
     */
    private long mTimestampDiff;

    /**
     * Initialize the connection using specified port and host
     */
    UdpConnection(String host, int port) throws IOException {
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
    public void close() {
        mSocket.close();
    }

    /**
     * Push new data to the remote partner
     */
    @Override
    public void onData(SensorData sensorData) {
        if(!mInitializationSent){
            System.out.println("system:" + new Date().getTime() * 1000000 + ", sensor: " + sensorData.timestamp);
            mTimestampDiff = new Date().getTime() * 1000000 - sensorData.timestamp;
            mInitializationSent = true;
        }
        new SensorOut().execute(sensorData);
    }


    /**
     * Class to encapsulate sending SensorData objects asynchronously
     */
    private class SensorOut extends AsyncTask<SensorData, Void, Boolean> {

        @Override
        protected Boolean doInBackground(SensorData... sensorData) {
            try {
                sensorData[0].timestamp += mTimestampDiff;
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
