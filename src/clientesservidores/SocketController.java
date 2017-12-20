/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientesservidores;

/**
 *
 * @author User
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import jdk.nashorn.internal.ir.CatchNode;

/**
 *
 * @author User
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.ir.CatchNode;

/**
 *
 * @author User
 */
    public class SocketController implements Runnable {

    private Thread theThread = null;
    public Socket theSocket = null;
    private PrintWriter theOut = null;
    private BufferedReader theIn = null;
    private String host;
    private int port;
    
    private Agent agente;
    
    
    public SocketController(Socket newSocket) {
        theSocket = newSocket;
        try {

            theOut = new PrintWriter(theSocket.getOutputStream(), true);
            theIn = new BufferedReader(new InputStreamReader(theSocket.getInputStream(), "UTF-8"));

        } catch (IOException ex) {
            Logger.getLogger(SocketController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        theThread = new Thread(this);
        theThread.start();
    }
    
    public SocketController(String hostname, int port)
    {
        this.port= port;
        this.host=hostname;
        
        
    }

    public Agent getAgente() {
        return agente;
    }

    public void setAgente(Agent agente) {
        this.agente = agente;
    }
    
    
    
     public void Open() {    
        try {
            this.theSocket = new Socket(this.host, this.port);
            this.theOut = new PrintWriter(this.theSocket.getOutputStream(), true);
            this.theIn = new BufferedReader(new InputStreamReader(
                    this.theSocket.getInputStream(), "UTF-8"));
        } catch (IOException ex) {
            System.out.println("No se pudo conectar a:"+this.host+" ,"+this.port);
        }
        
        Thread hilo= new Thread(this);
        hilo.start();
    }

    public void close() {
        try {
            theIn.close();
            theOut.close();
            theSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void writeText(String text)
    {
        theOut.println(text);
    }
    
    
    public String readText() {
       

        try {
           return theIn.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SocketController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override

    public void run() {
        String command = null;
        boolean quit = false;
       // commandProcessor comProc= new commandProcessor();
         writeText("Hola Soy Andrés Fernando 1701411587");
        while (!quit) {
           
            command = readText();
            System.out.println(command);
            if (command != null) {
                //writeText(command);
                if(command.startsWith(""+Codigos.LISTA)){
                    try {
                        agente.dividirIPs(command.substring(5));
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(SocketController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    if (command.trim().toUpperCase().equals("QUIT")) {
                    quit = true;
                }
                else
                {
                  //  writeText(comProc.responseCommand(command));
                }
            }
        }
        close();
    }

}
