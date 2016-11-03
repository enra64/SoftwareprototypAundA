import java.io.*;
import java.net.*;
import java.util.*;


import sp_common.*;

public class Server {
    public static Logger logger;

	public static void main(String args[]) throws Exception {




		
		DatagramSocket serverSocket = new DatagramSocket();
		serverSocket.setReuseAddress(true);
		serverSocket.bind(new InetSocketAddress(12345));

		try {

            logger = Logger.getLogger();
			byte[] appData = new byte[1024];
			System.out.println("running");

			while (true) {
                long start = new Date().getTime();

				DatagramPacket appPacket = new DatagramPacket(appData,
						appData.length);
				serverSocket.receive(appPacket);
				ByteArrayInputStream input = new ByteArrayInputStream(appData);
				ObjectInputStream oinput = new ObjectInputStream(input);
				SensorData sensorData = (SensorData) oinput.readObject();
				
				System.out.println("SensorType: " + sensorData.sensorType);
				System.out.println("Data: " + sensorData.data);
				System.out.println("Timestamp: " + sensorData.timestamp);
				System.out.println("Accuracy: " + sensorData.accuracy);
                long end = new Date().getTime();
				
				logger.write(String.valueOf("Timestamp"+sensorData.timestamp));
                logger.write(end-start+"");



			}
			
		} finally {
			serverSocket.close();
			logger.close();
		}
	}
}
