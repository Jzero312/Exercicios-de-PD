package ex7_v5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCPex7 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(9001);

        while(true)
        {
            Socket sCli = ss.accept();
            ThreadClient tc = new ThreadClient(sCli);
            Thread t1 = new Thread(tc);
            t1.start();
        }
    }
}
