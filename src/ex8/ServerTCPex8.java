package ex8;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCPex8 {
    private static final String SERVER_DIR = "./ServerFiles/";
    private static final int MAX_DATA = 4000;

    public static void main(String[] args) throws IOException
    {
        ServerSocket ss = new ServerSocket(9001);

        while(true)
        {
            Socket sCli = ss.accept();
            InputStream in = sCli.getInputStream();
            OutputStream out = sCli.getOutputStream();

            byte[] bytesRead = new byte[MAX_DATA];
            int nBytes = in.read(bytesRead);
            String filename = new String(bytesRead, 0, nBytes);

            FileInputStream fis = new FileInputStream(SERVER_DIR + filename);
            while(fis.available() > 0)
            {
                byte[] fileChuck = new byte[MAX_DATA];
                nBytes = fis.read(fileChuck);
                out.write(fileChuck, 0, nBytes);
                out.flush();
            }


            System.out.println("Send file '" + filename + "' to " + sCli.getInetAddress().getHostAddress() + " : " + sCli.getPort());

            out.close();
            in.close();
            sCli.close();

        }
    }
}
