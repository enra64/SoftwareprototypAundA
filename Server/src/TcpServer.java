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
     * Program entry point: starts a new server that may be stopped by entering any character in terminal. The server
     * will collect some statistics (mostly timestamps) and save them to statistics.csv after stopping.
     */
    public static void main(String args[]) {
        /*
         * A list of all received SensorData elements; the indices correspond directly to the receipt timestamps stored
         * in sensorDataReceiptTimestamps
         */
        final List<SensorData> sensorDataHistory = new ArrayList<>();

        /*
          A list of SensorData receipt timestamps [ns]; the indices correspond directly to the SensorData elements of
          sensorDataHistory
         */
        final List<Long> sensorDataReceiptTimestamps = new ArrayList<>();

        // create the server; the listener should insert into the lists declared above
        ServerThread server = new ServerThread(new ServerThread.OnUpdateListener() {
            @Override
            public void onUpdate(SensorData sensorData) {
                sensorDataHistory.add(sensorData);
                sensorDataReceiptTimestamps.add(new Date().getTime() * 1000000);
            }
        });

        // start the server
        server.start();

        // when the user inputs data, this will be skipped, and we go to shutdown
        Scanner killScanner = new Scanner(System.in);
        killScanner.nextLine();

        // gracefully shutdown server
        server.shutdown();

        // save gathered results
        save(sensorDataHistory, sensorDataReceiptTimestamps);
    }

    /**
     * Save all SensorData objects and their corresponding server receipt timestamps
     */
    private static void save(List<SensorData> sensorDataHistory, List<Long> timestampHistory) {
        // both must have the same size, as their indices must directly correspond
        assert(sensorDataHistory.size() == timestampHistory.size());

        try (FileWriter fw = new FileWriter("tcp_statistics.csv", false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            // iterate over all history
            for (int i = 0; i < sensorDataHistory.size(); i++) {
                out.print(sensorDataHistory.get(i) + ",");
                out.println(timestampHistory.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * The ServerThread class is the actual workhorse of the TcpServer class;
     */
    static class ServerThread extends Thread {
        /**
         * Interface for ServerThread update listeners
         */
        interface OnUpdateListener {

            /**
             * Called whenever the server has received a new SensorData object
             */
            void onUpdate(SensorData sensorData);
        }

        /**
         * Our listener; called whenever a new SensorData object is received
         */
        OnUpdateListener mListener;

        /**
         * true if the server should continue running
         */
        private boolean mRunning = true;

        /**
         * Create a new ServerThread with an OnUpdateListener that will be called whenever a new object arrived
         * @param listener
         */
        ServerThread (OnUpdateListener listener){
            mListener = listener;
        }

        /**
         * Gracefully stop the server asap
         */
        void shutdown() {
            // kill the running server
            mRunning = false;
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
                    // call listener with new object
                    mListener.onUpdate((SensorData) oinput.readUnshared());
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
            }
        }
    }
}
