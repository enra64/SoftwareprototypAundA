package de.oerntec.udprototype;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

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
    private TcpThread mSocketThread;

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
    TcpConnection(String host, int port) throws IOException{
        mSocketThread = new TcpThread(host, port);
        mSocketThread.start();
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
        if(!mInitializationSent){
            System.out.println("system:" + new Date().getTime() * 1000000 + ", sensor: " + sensorData.timestamp);
            mInitializationSent = true;
        }
        mSocketThread.send(sensorData);
    }

    private class TcpThread extends Thread {
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
         * A queue of messages that still have to be sent
         */
        private Queue<SensorData> mDataQueue = new ArrayDeque<>();

        /**
         * Initialize the connection using specified port and host
         */
        TcpThread(String host, int port) throws IOException {
            // save host(translated) and port
            mHost = InetAddress.getByName(host);
            mPort = port;
        }

        @Override
        public void run() {
            ObjectOutputStream objectOutputStream = null;
            try {
                // create socket from host and port
                mSocket = new Socket(mHost, mPort);

                // get an object output stream
                objectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());

                // send every object we get
                while(true) {
                    if (!mDataQueue.isEmpty()){
                        SensorData data = mDataQueue.poll();
                        if(data != null) {
                            // write unshared to ensure new objects are written to the stream
                            objectOutputStream.writeUnshared(data);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // close the output stream after finishing running
                try {
                    if(objectOutputStream != null)
                        objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        void send(SensorData data){
            // use clone() to correctly copy its float[] member
            mDataQueue.add(data.clone());
        }
    }
}
