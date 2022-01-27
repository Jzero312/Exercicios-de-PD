package ex8;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTCPex8 {
    private static final int MAX_DATA = 4000;
    private static final String FILE_NAME = "cartaz.png";
    private static final String CLIENT_DIR = "./ClienteFiles/";

    public static void main(String[] args) throws IOException {

        Socket sCli = new Socket("localhost", 9001);
        InputStream in = sCli.getInputStream();
        OutputStream out = sCli.getOutputStream();

        byte[] fileNameBytes = FILE_NAME.getBytes();
        out.write(fileNameBytes);
        out.flush();
        //----------------

        FileOutputStream fos = new FileOutputStream(CLIENT_DIR + FILE_NAME);
        int nBytes = 0;
        byte[] bytesRead = new byte[MAX_DATA];
        while((nBytes = in.read(bytesRead)) != -1)
        {
            fos.write(bytesRead, 0, nBytes);
            bytesRead = new byte[MAX_DATA];
            //fos.write(bytesRead);
        }

        System.out.println("Receive file '" + FILE_NAME + "' from " +
                sCli.getInetAddress().getHostAddress() + " : " + sCli.getPort());

        in.close();
        out.close();
        sCli.close();



    }

}
