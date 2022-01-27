package ex11;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ChatClient
{
    private final String MULTICAST_IP = "239.1.2.3";
    private final int MULTICAST_PORT = 9009;
    private MulticastSocket ms;
    private String username;

    public void start() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        username = scanner.nextLine();

        ms = new MulticastSocket(MULTICAST_PORT);
        InetAddress mulIp = InetAddress.getByName(MULTICAST_IP);
        //InetSocketAddress tb suporta passar por string
        //o unico PORT que interesssa é o que utilizamos para a construçao do Multicast
        InetSocketAddress isa = new InetSocketAddress(mulIp, MULTICAST_PORT);
        NetworkInterface ni = NetworkInterface.getByName("wlan1");
        ms.joinGroup(isa, ni);

        ThreadReadMessages trm = new ThreadReadMessages();
        trm.start();

        System.out.println("Chat is online. Type 'quit' to exit.");
        while(true){
            String msg = scanner.nextLine();
            if(msg.equals("quit")){
                ms.leaveGroup(isa, ni);
                ms.close();
                break;
            }

            msg = "[" + username + "] " + msg;
            byte[] msgBytes = msg.getBytes();
            DatagramPacket dp = new DatagramPacket(
                    msgBytes, msgBytes.length, mulIp, MULTICAST_PORT);
            ms.send(dp);
        }
        //espera pela thread
        trm.join();


    }

    class ThreadReadMessages extends Thread
    {
        @Override
        public void run() {
            try {
                while (true)
                {
                    DatagramPacket dp = new DatagramPacket(new byte[512], 512);
                    ms.receive(dp);
                    String msg = new String(dp.getData(), 0, dp.getLength());
                    System.out.println("[" + dp.getAddress().getHostAddress() + ":" + dp.getPort() + "]" + msg);

                    if(msg.endsWith("LIST")){
                        dp.setData(username.getBytes());
                        dp.setLength(username.getBytes().length);
                        ms.send(dp);
                    }
                }
            }catch (IOException e)
            {
                System.out.println("Chat is closing...");
            }

        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ChatClient cc = new ChatClient();
        cc.start();
    }
}
