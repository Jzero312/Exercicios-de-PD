package ex5p1;

import java.io.IOException;
import java.net.*;

public class ClienteUDPEx5
{
    public static void main(String[] args) throws IOException {
        //mandar msg para o servidor
        DatagramSocket ds = new DatagramSocket();

        byte[] timeReq = "TIME".getBytes();

        InetAddress ip = InetAddress.getByName("127.0.0.1");
        DatagramPacket dp = new DatagramPacket(
                timeReq, timeReq.length, ip, 9001);

        ds.send(dp);

        //receber msg do servidor
        //nesta parte podemos criar um novo datapackge ou simplesmente referenciar o dp ja criado para um novo
        dp = new DatagramPacket(new byte[256], 256);
        ds.receive(dp);
        String msgReceived = new String(dp.getData(), 0, dp.getLength());

        System.out.println("Receive from " + dp.getAddress().getHostAddress() + " port: " + dp.getPort() + " at " + msgReceived);

        //boa pratica fechar o socket
        ds.close();
    }
}
