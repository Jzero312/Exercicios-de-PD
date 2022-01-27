package ex8_v2;

import java.io.*;
import java.net.Socket;

public class ClientTCPex8 {
    private static final int MAX_DATA = 4000;
    private static final String FILE_NAME = "cartaz.png";
    private static final String CLIENT_DIR = "./ClienteFiles/";

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Socket sCli = new Socket("localhost", 9001);
        ObjectOutputStream out = new ObjectOutputStream(sCli.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(sCli.getInputStream());

        //byte[] fileNameBytes = FILE_NAME.getBytes();
        out.writeObject(FILE_NAME);
        out.flush();
        //----------------

        FileOutputStream fos = new FileOutputStream(CLIENT_DIR + FILE_NAME);
//        int nBytes = 0;
//        byte[] bytesRead = new byte[MAX_DATA];
//        while((nBytes = in.read(bytesRead)) != -1)
//        {
//            fos.write(bytesRead, 0, nBytes);
//            bytesRead = new byte[MAX_DATA];
//            //fos.write(bytesRead);
//        }
        int nBytes = 0;
        byte[] bytesRead = new byte[MAX_DATA];
        while((bytesRead = (byte[]) in.readObject()) != null)
        {
            fos.write(bytesRead, 0, nBytes);
            bytesRead = new byte[MAX_DATA];
        }


        System.out.println("Receive file '" + FILE_NAME + "' from " +
                sCli.getInetAddress().getHostAddress() + " : " + sCli.getPort());

        in.close();
        out.close();
        sCli.close();



    }

}
