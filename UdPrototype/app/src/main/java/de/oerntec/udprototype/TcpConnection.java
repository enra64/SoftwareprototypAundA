package de.oerntec.udprototype;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.DatagramChannel;

import sp_common.DataSink;
import sp_common.SensorData;

/**
 * The UdpConnection class is an UDP implementation of the DataSink interface; it sends all input
 * data to the host and port given in the constructor
 */

class TcpConnection implements DataSink {
    /**
     * the thread used for communicating via tcp
     */
    TcpThread mSocketThread;

    /**
     * Initialize the connection using specified port and host
     */
    TcpConnection(String host, int port) throws IOException{
        mSocketThread = new TcpThread(host, port);
    }

    /**
     * call before exiting
     */
    @Override
    public void close() {

    }

    /**
     * Push new data to the remote partner
     */
    @Override
    public void onData(SensorData sensorData) {
        mSocketThread.send(sensorData);
    }

    private class TcpThread implements Runnable {
        /**
         * The socket we use for sending data; initialized in the constructor
         */
        private Socket mSocket;

        /**
         * The target host for our UDP packets
         */
        private InetAddress mHost;

        /**
         * The target port on {@link #mHost} for our UDP packets
         */
        private final int mPort;

        /**
         * Initialize the connection using specified port and host
         */
        TcpThread(String host, int port) throws IOException {
            // save host(translated) and port
            mHost = InetAddress.getByName(host);
            mPort = port;

            // create socket from host and port
            mSocket = new Socket(mHost, mPort);
        }

        @Override
        public void run() {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void send(SensorData data){
            try {
                notify();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(mSocket.getOutputStream());
                os.writeObject(data);
                wait();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
