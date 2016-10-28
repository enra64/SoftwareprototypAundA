import java.io.*;
import java.net.*;

import sp_common.*;

public class Server {
    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] appData = new byte[1024];
        System.out.println("running");
        while (true) {
            DatagramPacket appPacket = new DatagramPacket(appData, appData.length);
            serverSocket.receive(appPacket);
            ByteArrayInputStream input = new ByteArrayInputStream(appData);
            ObjectInputStream oinput = new ObjectInputStream(input);
            SensorData sensorData = (SensorData) oinput.readObject();
            System.out.println("SensorType: " + sensorData.sensorType);
            System.out.println("Data: " + sensorData.data);
            System.out.println("Timestamp: " + sensorData.timestamp);
            System.out.println("Accuracy: " + sensorData.accuracy);

            // Address already in use: Cannot bind Error

            // serverSocket.send(appPacket);
        }
    }
}
