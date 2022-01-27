package ex18;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FileClientex18 extends UnicastRemoteObject implements GetRemoteFileClientInterface {
    private final String CLIENT_DIR = "./ClienteFiles";
    private static final String FILENAME = "cartaz.png";

    private FileOutputStream fos;

    protected FileClientex18() throws RemoteException, FileNotFoundException {
        File f = new File(CLIENT_DIR, FILENAME);
        fos = new FileOutputStream(f);
    }


    @Override
    public void writeFileChunk(byte[] fileChunk, int nbytes) throws IOException {
        if(fileChunk == null)
        {
            fos.flush();
            fos.close();
            System.out.println("File was downloaded...");
            //como termina o download podemos terminar o processo
            unexportObject(this, true);
            return;
        }

        fos.write(fileChunk, 0, nbytes);
    }

    public static void main(String[] args)
    {
        try {

            Registry r = LocateRegistry.getRegistry("127.0.0.1", Registry.REGISTRY_PORT);
            GetRemoteFileServerInterface fileServerInterface = null;
            fileServerInterface = (GetRemoteFileServerInterface) r.lookup("fileserver");
            FileClientex18 fileClientObj = new FileClientex18();
            fileServerInterface.getFile(FILENAME, fileClientObj);

        } catch (NotBoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
