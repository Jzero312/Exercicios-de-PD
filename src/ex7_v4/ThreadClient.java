package ex7_v4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;

public class ThreadClient extends Thread
{
    private Socket sCli;

    public ThreadClient(Socket sCli) {
        this.sCli = sCli;
    }

    @Override
    public void run()
    {
        try{
            ObjectInputStream in = new ObjectInputStream(sCli.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(sCli.getOutputStream());

            //byte[] bytesRead = new byte[256];
            //int nBytes = in.read(bytesRead);
            //String msgReceived = new String(bytesRead, 0, nBytes);

            String msgReceived = (String) in.readObject();

            if(msgReceived.equals("TIME"))
            {
                //String localTime = sdf.format(new Date());
                //byte[] bytesWrite = localTime.getBytes();
                //out.write(bytesWrite);
                Calendar cal = Calendar.getInstance();
                ServerTime st = new ServerTime(
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND));

                out.writeObject(st);
                //caso queira reenviar o mesmo objeto e alterar os dados (limpar cache)
                // out.reset(); --> limpa a cache toda
                // out.writeUnshared(st) --> aqui caga no objecto que esta na cache e envia o atual
                out.flush();

                System.out.println("Send to: " + sCli.getInetAddress().getHostAddress() + ":" + sCli.getPort() + " at " + st.toString());
            }


            out.close();
            in.close();
            sCli.close();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }


    }
}
