package ex7_exemplo_conected_log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

public class ThreadClient extends Thread
{
    private Socket sCli;
    private ArrayList<String> logs;

    public ThreadClient(Socket sCli, ArrayList<String> logs) {
        this.sCli = sCli;
        this.logs = logs;
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

                String logMsg = "Send to: " + sCli.getInetAddress().getHostAddress() + ":" + sCli.getPort() + " at " + st.toString();
                System.out.println(logMsg);
                synchronized (logs)
                {
                    logs.add(logMsg);
                }
                //addLog(logMsg);

            }


            out.close();
            in.close();
            sCli.close();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }


    }

    //nesta situaçao ñ garante sync no servidor
    public synchronized void addLog(String logMsg){
        logs.add(logMsg);
    }
}
