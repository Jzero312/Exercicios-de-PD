package ex7_exemplo_conected_log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTCPex7 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(9001);
        ArrayList<String> logs = new ArrayList<>();

        while(true)
        {
            Socket sCli = ss.accept();
            ThreadClient tc = new ThreadClient(sCli, logs);
            tc.start();

            synchronized (logs){
                for(String l : logs)
                    System.out.println(l);
            }
        }
    }
}
