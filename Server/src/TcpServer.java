import sp_common.SensorData;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * TCP prototype server implementation
 */
public class TcpServer {

    /**
     * Program entry point: starts a new server that may be stopped by entering any character in terminal
     */
    public static void main(String args[]) {
        // start server; async
        ServerThread server = new ServerThread();
        server.start();

        // when the user inputs data, this will be skipped, and we go to shutdown
        Scanner killScanner = new Scanner(System.in);
        killScanner.nextLine();

        // gracefully shutdown server
        server.shutdown();
    }

    /**
     * The ServerThread class is the actual workhorse of the TcpServer class;
     */
    static class ServerThread extends Thread {
        interface OnUpdateListener {
            void onUpdate(SensorData sensorData);
        }

        /**
         * A list of all received SensorData elements; the indices correspond directly to the receipt timestamps stored
         * in mSensorDataReceiptTimestamps
         */
        private List<SensorData> mSensorDataHistory = new ArrayList<>();

        /**
         * A list of SensorData receipt timestamps [ns]; the indices correspond directly to the SensorData elements of
         * mSensorDataHistory
         */
        private List<Long> mSensorDataReceiptTimestamps = new ArrayList<>();

        /**
         * true if the server should continue running
         */
        private boolean mRunning = true;

        /**
         * Gracefully stop the server asap
         */
        void shutdown() {
            // kill the running server
            mRunning = false;
        }

        /**
         * Save all SensorData objects and their corresponding server receipt timestamps
         */
        private void save() {
            try (FileWriter fw = new FileWriter("statistics.csv", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                // iterate over all history
                for (int i = 0; i < mSensorDataHistory.size(); i++) {
                    out.print(mSensorDataHistory.get(i) + ",");
                    out.println(mSensorDataReceiptTimestamps.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        /**
         * Listen on socket;
         */
        @Override
        public void run() {
            // declare objects that will need to be closed
            ServerSocket listenerSocket = null;
            Socket connection = null;
            ObjectInputStream oinput = null;

            try {
                // listen for tcp connections
                listenerSocket = new ServerSocket(25456);

                // accept incoming connections
                connection = listenerSocket.accept();

                // notify user of working state
                System.out.println("connection on port " + listenerSocket.getLocalPort());

                // get an object input stream; this is open as long as connection is open,
                // so the tcp connection stays open as long as Data is transmitted
                oinput = new ObjectInputStream(connection.getInputStream());

                // read objects on the same connection until shutdown() is called
                while (mRunning) {
                    // store SensorData object and receipt timestamp [ns]
                    SensorData s = (SensorData) oinput.readUnshared();

                    if(s != null){
                        mSensorDataHistory.add(s);
                        mSensorDataReceiptTimestamps.add(new Date().getTime() * 1000000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // close all
                try {
                    if(oinput != null)
                        oinput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(connection != null)
                        connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(listenerSocket != null)
                        listenerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // save history for analysis
                save();
            }
        }
    }
}
