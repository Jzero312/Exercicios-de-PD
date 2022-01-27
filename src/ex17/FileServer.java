package ex17;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import java.util.Arrays;

public class FileServer extends UnicastRemoteObject implements RemoteFileInterface
{

    private final String SERVER_DIR = "./ServerFiles";

    protected FileServer() throws RemoteException { }

    @Override
    public byte[] getFileChunk(String fileName, long offset) throws IOException {

//        try
//        {
            File f = new File(SERVER_DIR, fileName);
            FileInputStream fis = new FileInputStream(f);
            byte[] fileChuck = new byte[512];
            fis.skip(offset);
            int nBytes = fis.read(fileChuck);
            if(nBytes > -1)
                return Arrays.copyOf(fileChuck, nBytes);
            return null;
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args)
    {
        try {
            Registry r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            FileServer fs = new FileServer();
            r.rebind("fileserver", fs);
            System.out.println("System is running...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
