package ex14;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//Esta classe tamb�m representa uma thread
public class DistributedPiWorkerComSerializacao extends Thread
{
    protected Socket s;
    
    public DistributedPiWorkerComSerializacao(Socket s)
    {
        this.s = s;
    }
    
    public double getMyResult(int myId, int nWorkers, long nIntervals)
    {
        long i;
        double dX, xi, myResult;
        
        if(nIntervals < 1 || nWorkers < 1 || myId <1 || myId > nWorkers)
            return 0.0;

        
        dX = 1.0/nIntervals;
        myResult = 0;
        
        for (i = myId-1 ; i < nIntervals; i += nWorkers) {             
            xi = dX*(i + 0.5);
            myResult += (4.0/(1.0 + xi*xi));               
        }
        
        myResult *= dX;
        
        return myResult;
    }
    
    @Override
    public void run()
    {
        int myId;
        int nWorkers;
        long nIntervals;
        double myResult;
        RequestToWorker req;
        
        //Cria um ObjectInputStream e um ObjectOutputStream associados ao socket s       
        try(ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream()))
        {
                                        
            req = (RequestToWorker) in.readObject(); //Aguarda pela recepcao de um pedido enviado pelo master

            myId = req.getId();
            nWorkers = req.getnWorkers();
            nIntervals = req.getnIntervals();
            
            myResult = getMyResult(myId, nWorkers, nIntervals);
            
            //Envia myResult ao Master sob a forma de um Double serializado
            out.writeObject(myResult);
            out.flush();
            
            System.out.format("<%s> %.10f\n", Thread.currentThread().getName(), myResult);
            
        }
        catch(ClassNotFoundException e)
        {
                System.err.println("<" + Thread.currentThread().getName() + ">: " + e);             
                return;
        }
        catch(IOException e)
        {
            System.out.println("<" + Thread.currentThread().getName() + 
                    "> Erro ao aceder ao socket:\n\t" + e);
        }
        finally
        {
            try
            {
                if(s != null)
                    s.close();
            }
            catch(IOException e){}
        }
        
    }

    public static void main(String[] args) {
        
        Socket toMaster;
        int listeningPort;
        Thread t;
        int nCreatedThreads = 0;
        
        if(args.length != 1)
        {
            System.out.println("Sintaxe: java DistributedPiWorker <listening port>");
            return;
        }
        
        listeningPort = Integer.parseInt(args[0]);
        
        try(ServerSocket s = new ServerSocket(listeningPort))
        {
                        
            while(true)
            {
                //Aceita um pedido de ligacao TCP de um master
                toMaster = s.accept();

                //Inicia uma thread destinada a tratar da comunicacao com o master
                nCreatedThreads++;
                t = new Thread(new DistributedPiWorkerComSerializacao(toMaster), "Thread_" + nCreatedThreads);
                t.start();
            }            
            
        }
        catch(IOException e)
        {
            System.out.println("<DistributedPiWorker> Erro ao aceder ao socket:\n\t" + e);
        }
    }
}
