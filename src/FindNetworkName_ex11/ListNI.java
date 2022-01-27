package FindNetworkName_ex11;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ListNI
{
    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
        while (list.hasMoreElements()){
            NetworkInterface ni = list.nextElement();
            System.out.println(ni.getName());
            Enumeration<InetAddress> listIA = ni.getInetAddresses();
            while (listIA.hasMoreElements()){
                System.out.println(listIA.nextElement().getHostAddress());
            }
            System.out.println();
        }
    }
}
