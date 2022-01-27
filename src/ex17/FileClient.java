package ex17;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FileClient
{
    private static final String CLIENT_DIR = "./ClienteFiles";
    private static final String FILENAME = "cartaz2.png";

    public static void main(String[] args)
    {
        try
        {
            Registry r = LocateRegistry.getRegistry("127.0.0.1", Registry.REGISTRY_PORT);
            RemoteFileInterface rfi = (RemoteFileInterface) r.lookup("fileserver");

            File f = new File(CLIENT_DIR, FILENAME);
            FileOutputStream fos = new FileOutputStream(f);
            long offset = 0;
            byte[] fileChuck;

            do
            {
                fileChuck = rfi.getFileChunk(FILENAME, offset);

                if(fileChuck == null)
                {
                    //estou no fim do ficheiro
                    fos.flush();
                    fos.close();
                    System.out.println("File " + FILENAME + " was downloaded.");
                }
                else
                {
                    fos.write(fileChuck);
                    offset += fileChuck.length;
                }


            } while (fileChuck != null);

        } catch (NotBoundException | IOException e) {
            e.printStackTrace();
        }

    }
}
