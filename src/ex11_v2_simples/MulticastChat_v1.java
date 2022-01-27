/*Neste exemplo, a informacao trocada e' apenas uma cadeia de caracteres*/

/*
   Multicast IPv4 em Mac OS
   Na lina de comando: -Djava.net.preferIPv4Stack=true
   No código: System.setProperty("java.net.preferIPv4Stack", "true");
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
//import java.util.Enumeration;

public class MulticastChat_v1 extends Thread
{
    public static final String LIST = "LIST";
    public static final String EXIT = "EXIT";
    public static final int MAX_SIZE = 1000;
    
    protected String username;
    protected MulticastSocket s = null;
    protected boolean running = false;

    public MulticastChat_v1(String username, MulticastSocket s) 
    {
        this.username = username;
        this.s = s;
        running = true;
    }
    
    public void terminate()
    {                
        running = false;
    }
            
    @Override
    public void run()
    {
        DatagramPacket pkt;
        String msg;
        
        if(s == null || !running){
            return;
        }
        
        try{
            
            while(running){
                
                pkt = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                s.receive(pkt);
                
                msg = new String(pkt.getData(), 0, pkt.getLength());
                
                if(msg.toUpperCase().contains(LIST.toUpperCase())){
                    pkt.setData(username.getBytes());
                    pkt.setLength(username.length());
                    s.send(pkt);
                    //continue;
                }
                
                System.out.println();
                System.out.println("(" + pkt.getAddress().getHostAddress() + ":" + pkt.getPort() + ") " + msg);
                System.out.println(); System.out.print("> ");
                
            }
            
        }catch(IOException e){
            
            if(running){
                System.out.println(e);
            }
            
            if(!s.isClosed()){                
                s.close();
            }
            
        }                
    }
    
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
     
        InetAddress group;
        int port;
        MulticastSocket socket = null;
        DatagramPacket dgram;
        String msg, msgToSend;        
        MulticastChat_v1 t = null;
        NetworkInterface nif;
                        
        //System.setProperty("java.net.preferIPv4Stack", "true"); //Mac OS
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        if(args.length != 4){
            System.out.println("Sintaxe: java MulticastChat <nickname> <groupo multicast> <porto> <NIC multicast>");
            return;
        }
        
        try{
            group = InetAddress.getByName(args[1]);
            port = Integer.parseInt(args[2]);
            
            /*Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()){
                NetworkInterface interf = interfaces.nextElement();
                System.out.print(interf.getName());
                try{
                    System.out.println(" : " + interf.getInetAddresses().nextElement().getHostAddress());
                }catch(Exception ex){
                    System.out.println();
                }
            }*/
                                    
            try{                
                nif = NetworkInterface.getByInetAddress(InetAddress.getByName(args[3])); //e.g., 127.0.0.1, 192.168.10.1, ... 
            }catch (SocketException | NullPointerException | UnknownHostException | SecurityException ex){               
                nif = NetworkInterface.getByName(args[3]); //e.g., lo, eth0, wlan0, en0, ...
            }
                        
            socket = new MulticastSocket(port);
            
            //socket.setNetworkInterface(nif);            
            //socket.joinGroup(group);
            
            socket.joinGroup(new InetSocketAddress(group, port), nif);
            
            t = new MulticastChat_v1(args[0], socket);
            //t.setDaemon(true);
            t.start();
            
            System.out.print("> ");
            
            while(true){
                
                msg = in.readLine();
                
                if(msg.equalsIgnoreCase(EXIT)){
                    break;
                }
                             
                msgToSend = args[0] + ": " + msg;
                        
                dgram = new DatagramPacket(msgToSend.getBytes(), msgToSend.length(), group, port);
                socket.send(dgram); 
                
            }
            
        }finally{
            
            if(t != null){
                t.terminate();
            }
            
            if(socket != null){
                socket.close();
            }
            
            //t.join(); //Para esperar que a thread termine caso esteja em modo daemon
            
        }

    }
}
