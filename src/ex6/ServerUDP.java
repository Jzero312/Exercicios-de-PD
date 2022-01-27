package ex6;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerUDP {
    private static final String SERVER_DIR = "./ServerFiles/";
    private static final int MAX_DATA = 4000;

    public static void main(String[] args) throws IOException
    {
        DatagramSocket ds = new DatagramSocket(9002);

        while(true)
        {
            DatagramPacket dp = new DatagramPacket(new byte[256], 256);
            ds.receive(dp);
            String filename = new String(dp.getData(), 0, dp.getLength());

            FileInputStream fis = new FileInputStream(SERVER_DIR + filename);
            while(fis.available() > 0)
            {
                byte[] fileChuck = new byte[MAX_DATA];
                int nBytes = fis.read(fileChuck);
                dp.setLength(nBytes);
                ds.send(dp);
            }
            dp.setLength(0);
            ds.send(dp);

            System.out.println("Send file '" + filename + "' to " + dp.getAddress().getHostAddress() + " port: " + dp.getPort());

        }
    }
}
