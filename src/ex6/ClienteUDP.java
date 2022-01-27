package ex6;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClienteUDP {
    private static final int MAX_DATA = 4000;
    private static final String FILE_NAME = "cartaz.png";
    private static final String CLIENT_DIR = "./ClienteFiles/";

    public static void main(String[] args) throws IOException
    {
        DatagramSocket ds = new DatagramSocket();
        byte[] fileNameBytes = FILE_NAME.getBytes();
        InetAddress ip = InetAddress.getByName("localhost");
        int port = 9002;
        DatagramPacket dp = new DatagramPacket(fileNameBytes, fileNameBytes.length, ip, port);
        ds.send(dp);

        FileOutputStream fos = new FileOutputStream(CLIENT_DIR + FILE_NAME);
        do
        {
            dp.setData(new byte[MAX_DATA]);
            dp.setLength(MAX_DATA);
            ds.receive(dp);
            fos.write(dp.getData(), 0, dp.getLength());

        } while(dp.getLength() > 0);

        fos.flush();
        fos.close();
        ds.close();

        System.out.println("Receive file '" + FILE_NAME + "' from " +
                dp.getAddress().getHostAddress() + " port: " + dp.getPort());

    }
}
