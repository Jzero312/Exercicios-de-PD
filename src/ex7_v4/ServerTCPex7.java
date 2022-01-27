package ex7_v4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerTCPex7 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(9001);

        while(true)
        {
            Socket sCli = ss.accept();
            ThreadClient tc = new ThreadClient(sCli);
            tc.start();
             //encerrar o socket encerra o in/out automaticamente
        }
    }
}
