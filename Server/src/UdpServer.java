import sp_common.SensorData;

import java.io.*;
import java.net.*;
import java.util.*;


public class UdpServer {
    private static Logger mLogger;

    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(null);
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(25456));
        boolean connected = false;

        try {
            // create result csv, write header
            mLogger = Logger.getLogger("udp_statistics.csv");
            mLogger.write("server ts, sensor ts");

            // storage for udp data
            byte[] appData = new byte[1024];

            System.out.println("running UDP server");

            while (true) {
                // receive udp packet
                DatagramPacket appPacket = new DatagramPacket(appData, appData.length);

                serverSocket.receive(appPacket);

                if(connected==false){
                    System.out.println("Connected");
                }

                connected = true;


                // parse incoming SensorData object
                ByteArrayInputStream input = new ByteArrayInputStream(appData);
                ObjectInputStream oinput = new ObjectInputStream(input);
                SensorData sensorData = (SensorData) oinput.readObject();

                // append new timestamp
                mLogger.write(String.valueOf(sensorData.timestamp) + "," + new Date().getTime()*1000000);
            }

        } finally {
            serverSocket.close();
            mLogger.close();
        }
    }

}
