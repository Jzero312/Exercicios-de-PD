package ex7p1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTCPex7 {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9001);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        while(true)
        {
            Socket sCli = ss.accept();
            InputStream in = sCli.getInputStream();
            OutputStream out = sCli.getOutputStream();

            byte[] bytesRead = new byte[256];
            int nBytes = in.read(bytesRead);
            String msgReceived = new String(bytesRead, 0, nBytes);

            if(msgReceived.equals("TIME"))
            {
                String localTime = sdf.format(new Date());
                byte[] bytesWrite = localTime.getBytes();
                out.write(bytesWrite);
                out.flush();

                System.out.println("Send to: " + sCli.getInetAddress().getHostAddress() + ":" + sCli.getPort() + " at " + localTime);
            }


            out.close();
            in.close();
            sCli.close(); //encerrar o socket encerra o in/out automaticamente
        }
    }
}
