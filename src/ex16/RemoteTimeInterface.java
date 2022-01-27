package ex16;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteTimeInterface extends Remote
{
    Hour getServerHour() throws RemoteException;

}
