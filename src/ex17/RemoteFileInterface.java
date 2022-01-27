package ex17;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteFileInterface extends Remote
{
    byte [] getFileChunk(String fileName, long offset) throws IOException;
}
