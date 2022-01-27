package ex18;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FileServerEx18 extends UnicastRemoteObject implements GetRemoteFileServerInterface {

    private final String SERVER_DIR = "./ServerFiles";
    protected FileServerEx18() throws RemoteException { }

    @Override
    public void getFile(String fileName, GetRemoteFileClientInterface cliRef) throws IOException
    {
        File f = new File(SERVER_DIR, fileName);
        FileInputStream fis = new FileInputStream(f);
        byte[] fileChunk = new byte[512];
        int nBytes;

        while((nBytes = fis.read(fileChunk)) > -1)
        {
            cliRef.writeFileChunk(fileChunk, nBytes);
        }

        fis.close();
        cliRef.writeFileChunk(null, -1);
    }

    public static void main(String[] args)
    {
        Registry r = null;
        try
        {
            r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            FileServerEx18 fs = new FileServerEx18();
            r.rebind("fileserver", fs);
            System.out.println("File server is running... [ex18]");

        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
