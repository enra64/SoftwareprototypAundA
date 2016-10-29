import sp_common.SensorData;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * TCP prototype server implementation
 */
public class TcpServer {
    public static void main(String args[]) {
        ServerThread server = new ServerThread();
        server.run();

        Scanner killScanner = new Scanner(System.in);
        killScanner.nextLine();

        server.stop();
    }

    private static class ServerThread implements Runnable {
        private List<SensorData> mSensorDataHistory = new ArrayList<>();
        private List<Long> mSensorDataReceiptTimestamps = new ArrayList<>();
        private boolean mRunning = true;

        void stop(){
            // kill the running server
            mRunning = false;
        }

        private void save(){
            // write out all sensor data history
            try(FileWriter fw = new FileWriter("weighthistory.csv", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                // print all history we collected
                for(int i = 0; i < mSensorDataHistory.size(); i++){
                    out.print(mSensorDataHistory.get(i) + ",");
                    out.println(mSensorDataReceiptTimestamps.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        @Override
        public void run() {
            try {
                // listen for tcp connections
                ServerSocket listenerSocket = new ServerSocket(6789);

                // accept incoming connections
                Socket connection = listenerSocket.accept();

                System.out.println("connection on port " + listenerSocket.getLocalPort());

                // get an object input stream
                ObjectInputStream oinput = new ObjectInputStream(connection.getInputStream());

                // read objects on the same connection until stop() is called
                while (mRunning) {

                    // store the acquired sonsordata in the history lists
                    mSensorDataHistory.add((SensorData) oinput.readObject());

                    // save timestamp, too
                    mSensorDataReceiptTimestamps.add(System.currentTimeMillis());
                }

                // save captured data
                save();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
