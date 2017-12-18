/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientesservidores;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Agent implements Runnable{
    private Thread theThread=null;
    private ServerSocket theServerSocket  =null;
    private LinkedList <SocketController> theClients= new LinkedList<>();
    private boolean quit=false;
    private int port;

    public Agent(int newPort) throws IOException{
        theServerSocket = new ServerSocket(newPort);
        theThread= new Thread(this);
        this.port= newPort;
    }
    
    public void start(){
        theThread.start();
    }
    
    public  void stop(){
        quit= true;
        try{
            theServerSocket.close();
           
        }
        catch(IOException ex)
        {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE,null,ex);
        }
        theThread.stop();
    }
    
    public void sendAll(String text)
    {
        for (SocketController theClient : theClients) {
            theClient.writeText(mostrarIPS());
        }
    }
    public boolean connect(String aHostname)
    {
        SocketController socket = new SocketController(aHostname, this.port);
        socket.Open();
        theClients.add(socket);
        return true;
        
      
    }
    public String mostrarIPS()
    {
        String cadena=" ";
        for (SocketController cliente: theClients) {
           cadena+=cliente.theSocket.getInetAddress().getHostAddress()+" , " ;          
          
        }
         //cadena=cadena.substring(cadena.length()-1,cadena.length());
        return cadena;
    }
    
    
    @Override
    public void run() {
       Socket aSocket=null;
       
       while(!quit)
       {
          try{
              aSocket= theServerSocket.accept();
              SocketController socket= new SocketController(aSocket);
              System.out.println("nuevo");
              
              theClients.add(socket);
              //System.out.println(mostrarIPS());
              socket.writeText(mostrarIPS()+" nO JUEGUES CON NOSOTROS");
              //System.out.println(mostrarIPS());
          }catch (IOException ex) 
          {
              quit=true;
          }
       }
    }
    
}
