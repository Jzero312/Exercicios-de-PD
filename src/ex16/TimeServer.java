package ex16;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;

public class TimeServer extends UnicastRemoteObject implements  RemoteTimeInterface
{

    protected TimeServer() throws RemoteException { }

    @Override
    public Hour getServerHour() throws RemoteException
    {
        Calendar cal = Calendar.getInstance();
        Hour h = new Hour(
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND));
        System.out.println("Time Request: " + h.toString());
        return h;
    }

    public static void main(String[] args)
    {
        try
        {
            TimeServer ts = new TimeServer();
            //Naming.rebind("rmi://127.0.0.1:1099/timeServer", ts);
            Registry r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            r.rebind("timeserver", ts);

            System.out.println("Server is running....");
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
