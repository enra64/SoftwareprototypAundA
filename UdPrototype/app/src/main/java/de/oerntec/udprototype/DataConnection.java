package de.oerntec.udprototype;

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

import sp_common.DataSink;
import sp_common.SensorData;

/**
 * Created by arne on 10/17/16.
 */

public class DataConnection implements DataSink {
    DatagramSocket mSocket;
    InetAddress mHost;
    int mPort;

    /**
     * Initialize the connection using specified port
     */
    DataConnection(String host, int port) throws UnknownHostException, SocketException {
        // save host(translated) and port
        mPort = port;
        mHost = InetAddress.getByName(host);

        // create socket from host and port
        mSocket = new DatagramSocket(port, mHost);
    }

    /**
     * call before exiting
     */
    void close(){
        mSocket.close();
    }

    /**
     * Push new data to the remote partner
     */
    @Override
    public void onData(SensorData sensorData) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(sensorData);
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, mHost, mPort);
            mSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
