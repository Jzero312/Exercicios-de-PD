package ex7_v5;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTCPex7 {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket sCli = new Socket("localhost", 9001);
        ObjectOutputStream out = new ObjectOutputStream(sCli.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(sCli.getInputStream());

        /*byte[] bytesWrite = "TIME".getBytes();
        out.write(bytesWrite);*/
        out.writeObject("TIME");
        out.flush();

        /*byte[] bytesRead = new byte[256];
        int nBytes = in.read(bytesRead);
        String msgReceived = new String(bytesRead, 0, nBytes);
        String msgReceived = (String) in.readObject();*/
        ServerTime st = (ServerTime) in.readObject();

        System.out.println("Received from " + sCli.getInetAddress().getHostAddress() + ":" + sCli.getPort() + " - " + st.toString());

        in.close();
        out.close();
        sCli.close();
    }
}
