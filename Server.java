import java.net.*;
public class Server {
	 public static void main(String args[]) throws Exception
     {
        DatagramSocket serverSocket = new DatagramSocket(9876); //9876 ??
           byte[] appData = new byte[1024]; //Größe optional
           while(true)
              {
                 DatagramPacket appPacket = new DatagramPacket(appData, appData.length);
                 serverSocket.receive(appPacket); 
                 System.out.println("erhalten");
                  // serverSocket.send(appPacket);
              }
     }
}
