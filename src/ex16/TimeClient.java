package ex16;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TimeClient
{
    public  static void main(String[] args)
    {
        try {
            //Remote remObj = Naming.lookup("rmi://127.0.0.1:1099/timeServer");
            Registry r = LocateRegistry.getRegistry();
            Remote remObj = r.lookup("timeserver");
            RemoteTimeInterface rti = (RemoteTimeInterface) remObj;
            Hour h = rti.getServerHour();
            System.out.println("Server Local Time: " + h.toString());
        } catch (NotBoundException | RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
