package ex7p1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTCPex7 {

    public static void main(String[] args) throws IOException {
        Socket sCli = new Socket("localhost", 9001);
        InputStream in = sCli.getInputStream();
        OutputStream out = sCli.getOutputStream();

        byte[] bytesWrite = "TIME".getBytes();
        out.write(bytesWrite);
        out.flush();

        byte[] bytesRead = new byte[256];
        int nBytes = in.read(bytesRead);
        String msgReceived = new String(bytesRead, 0, nBytes);

        System.out.println("Received from " + sCli.getInetAddress().getHostAddress() + ":" + sCli.getPort() + " - " + msgReceived);

        in.close();
        out.close();
        sCli.close();
    }
}
