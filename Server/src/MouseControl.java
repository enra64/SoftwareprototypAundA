import sp_common.SensorData;

/**
 * Created by arne on 10/31/16.
 */
public class MouseControl implements TcpServer.ServerThread.OnUpdateListener {

    public static void main(String args[]) {
        MouseControl robot = new MouseControl();
        TcpServer.ServerThread server = new TcpServer.ServerThread(robot);
        server.start();
    }

    @Override
    public void onUpdate(SensorData sensorData) {

    }
}
