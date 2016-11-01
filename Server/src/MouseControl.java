import sp_common.SensorData;

import java.awt.*;
import java.util.Arrays;


public class MouseControl implements TcpServer.ServerThread.OnUpdateListener {
    private float[] mLinearAcceleration = new float[3];
    private float[] mGravity = new float[3];
    private final float ALPHA = 0.8f;
    private Robot mRobot;

    public static void main(String args[]) throws AWTException {
        MouseControl robot = new MouseControl();
        TcpServer.ServerThread server = new TcpServer.ServerThread(robot);
        server.start();
    }

    public MouseControl() throws AWTException {
        mRobot = new Robot();

        mRobot.setAutoDelay(40);
        mRobot.setAutoWaitForIdle(true);
    }

    @Override
    public void onUpdate(SensorData sensorData) {
        // Isolate the force of gravity with the low-pass filter.
        mGravity[0] = ALPHA * mGravity[0] + (1 - ALPHA) * sensorData.data[0];
        mGravity[1] = ALPHA * mGravity[1] + (1 - ALPHA) * sensorData.data[1];
        mGravity[2] = ALPHA * mGravity[2] + (1 - ALPHA) * sensorData.data[2];

        // Remove the gravity contribution with the high-pass filter.
        mLinearAcceleration[0] = sensorData.data[0] - mGravity[0];
        mLinearAcceleration[1] = sensorData.data[1] - mGravity[1];
        mLinearAcceleration[2] = sensorData.data[2] - mGravity[2];

        System.out.print("\r" + Arrays.toString(mLinearAcceleration));

        Point currentLocation = MouseInfo.getPointerInfo().getLocation();

        // left
        if(mLinearAcceleration[0] < -1){
            mRobot.mouseMove(currentLocation.x + 1, currentLocation.y);
        }
        // right
        else if (mLinearAcceleration[0] > 1){
            mRobot.mouseMove(currentLocation.x - 1, currentLocation.y);
        }
    }
}
