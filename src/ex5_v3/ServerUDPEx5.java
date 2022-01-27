package ex5_v3;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ServerUDPEx5
{
    //PS: é boa pratica reutilizar o objecto do datagramPackeget
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramSocket ds = new DatagramSocket(9001);

        while (true)
        {
            DatagramPacket dp = new DatagramPacket(new byte[256], 256);
            ds.receive(dp); // pode lançar uma IOException

            //getData devolve um array de bytes, por isso tem que ser transformado em string
            //String msgReceived = new String(dp.getData(), 0, dp.getLength());
            ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
            ObjectInputStream in = new ObjectInputStream(bais);
            String msgReceived = (String) in.readObject();

            if(msgReceived.equals("TIME")){
                Calendar cal = Calendar.getInstance();
                ServerTime st = new ServerTime(
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND)
                );

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(baos);
                out.writeObject(st);
                out.flush();
                byte[] localTimeBytes = baos.toByteArray();


                //reutilizaçao do objecto é melhor, mas criar um novo ñ deixa de estar correto
                dp.setData(localTimeBytes);
                dp.setLength(localTimeBytes.length);
                ds.send(dp);

                System.out.println("Send to " + dp.getAddress().getHostAddress() + " port: " + dp.getPort() + " at " + st.toString());


            }
        }
    }
}
