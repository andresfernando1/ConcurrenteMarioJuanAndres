/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientesservidores;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Agent implements Runnable {

    private Thread theThread = null;
    private ServerSocket theServerSocket = null;
    private LinkedList<SocketController> theClients = new LinkedList<>();
    private boolean quit = false;
    private int port;

    public Index index=null;
    
    public Agent(int newPort) throws IOException {
        theServerSocket = new ServerSocket(newPort);
        theThread = new Thread(this);
        this.port = newPort;
    }

    public void start() {
        theThread.start();
    }

    public void stop() {
        quit = true;
        try {
            theServerSocket.close();

        } catch (IOException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }
        theThread.stop();
    }

    public void sendAll(String text) {
        for (SocketController theClient : theClients) {
            theClient.writeText(text);
        }
    }

    public void setIndex(Index index) {
        this.index = index;
    }

     
    public void send(String ip,String text){
        
        for (SocketController theClient : theClients) {
            
            if(theClient.theSocket.getInetAddress().getHostAddress().equals(ip)){
                theClient.writeText(text);
            }
        }
    }
    
    
    public boolean connect(String aHostname) {
        SocketController socket = new SocketController(aHostname, this.port);
        socket.Open();
        theClients.add(socket);
        
        this.index.agregarValores(socket.theSocket.getInetAddress().getHostAddress());
        socket.setAgente(this);
        return true;

    }

    public void dividirIPs(String cadena) throws UnknownHostException {

        String vector[] = cadena.split(",");

        for (int i = 0; i < vector.length; i++) {
            if(!Inet4Address.getLocalHost().getHostAddress().equals(vector[i].trim())){
            if(!vector[i].trim().equals(""))
            if (existIP(vector[i].trim())) {
                connect(vector[i].trim());
            }
            }
        }

    }

    public boolean existIP(String ip) {

        for (SocketController theClient : theClients) {

            if (theClient.theSocket.getInetAddress().getHostAddress() == ip) {
                return false;
            }

        }

        return true;
    }

    public String mostrarIPS(){
        String cadena = " ";
        for (SocketController cliente : theClients) {
            cadena += cliente.theSocket.getInetAddress().getHostAddress() + " , ";

        }
        //cadena=cadena.substring(cadena.length()-1,cadena.length());
        return cadena;
    }

    @Override
    public void run() {
        Socket aSocket = null;

        while (!quit) {
            try {
                aSocket = theServerSocket.accept();
                SocketController socket = new SocketController(aSocket);
                
                this.index.agregarValores(aSocket.getInetAddress().getHostAddress());
                socket.setAgente(this);
                theClients.add(socket);
                
                socket.writeText(Codigos.LISTA + " " + mostrarIPS());

            } catch (IOException ex) {
                quit = true;
            }
        }
    }

}
