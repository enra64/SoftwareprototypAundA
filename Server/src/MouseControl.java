import sp_common.SensorData;

import java.awt.*;
import java.awt.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class MouseControl implements TcpServer.ServerThread.OnUpdateListener {
    private float[] mGravity = new float[3];
    private final float ALPHA = 0.8f;
    private Robot mRobot;
    private Average mAccelerationAverage = new Average(100, 3);
    private TcpServer.ServerThread mServer;
    private ArrayList<float[]> mHistory = new ArrayList<>();

    public static void main(String args[]) throws AWTException {
        MouseControl robot = new MouseControl();

    }

    private MouseControl() throws AWTException {
        mRobot = new Robot();

        mRobot.setAutoDelay(40);
        mRobot.setAutoWaitForIdle(true);

        mServer = new TcpServer.ServerThread(this);
        mServer.start();

        // when the user inputs data, this will be skipped, and we go to shutdown
        Scanner killScanner = new Scanner(System.in);
        killScanner.nextLine();


        // gracefully shutdown server
        mServer.shutdown();

        // save history tho
        save();
    }

    private void save() {
        try (FileWriter fw = new FileWriter("statistics.csv", false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            // print headers
            out.println("x,y,z");
            // iterate over all history
            for (int i = 0; i < mHistory.size(); i++) {
                out.println(Arrays.toString(mHistory.get(i)).replaceAll("[ \\[\\]]", ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void onUpdate(SensorData sensorData) {
        float[] linearAcceleration = new float[3];

        // Isolate the force of gravity with the low-pass filter.
        mGravity[0] = ALPHA * mGravity[0] + (1 - ALPHA) * sensorData.data[0];
        mGravity[1] = ALPHA * mGravity[1] + (1 - ALPHA) * sensorData.data[1];
        mGravity[2] = ALPHA * mGravity[2] + (1 - ALPHA) * sensorData.data[2];

        // Remove the gravity contribution with the high-pass filter.
        linearAcceleration[0] = sensorData.data[0] - mGravity[0];
        linearAcceleration[1] = sensorData.data[1] - mGravity[1];
        linearAcceleration[2] = sensorData.data[2] - mGravity[2];

        mAccelerationAverage.add(linearAcceleration);

        float[] average = mAccelerationAverage.getAverage();

        mHistory.add(average.clone());

        System.out.print("\r" + Arrays.toString(average));

        Point currentLocation = MouseInfo.getPointerInfo().getLocation();



        // left
        if (average[0] < -1) {
            mRobot.mouseMove(currentLocation.x + 1, currentLocation.y);
        }
        // right
        else if (average[0] > 1) {
            mRobot.mouseMove(currentLocation.x - 1, currentLocation.y);
        }
    }

    private class Average {
        float[][] mStorage;
        float[] mZeroPoint;
        boolean mIsZeroPointSaved = false;
        int mIndex = 0;

        Average(int averageCount, int fieldLength) {
            mStorage = new float[averageCount][fieldLength];
            mZeroPoint = new float[fieldLength];

            for(int i = 0; i < mZeroPoint.length; i++)
                mZeroPoint[i] = 0;
        }

        void add(float[] val) {
            // check for correct values
            assert (val.length == mStorage[0].length);

            if (mIndex + 1 >= mStorage.length){
                mIndex = 0;
                if(!mIsZeroPointSaved){
                    mZeroPoint = getAverage();
                    mIsZeroPointSaved = true;
                    System.out.println("created zero point as " + Arrays.toString(mZeroPoint));
                }
            }

            mStorage[mIndex++] = val;
        }

        float[] getAverage() {
            float[] sum = new float[mStorage[0].length];

            for (int i = 0; i < mStorage.length; i++){
                for(int f = 0; f < sum.length; f++){
                    sum[f] += mStorage[i][f];
                }
            }

            for(int i = 0; i < sum.length; i++)
                sum[i] = sum[i] / mStorage.length;

            float[] result = sum.clone();

            for(int i = 0; i < result.length; i++){
                //result[i] -= mZeroPoint[i];
            }

            return result;
        }
    }
}
