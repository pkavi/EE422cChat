package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


public class ClientThread  implements Runnable  {
private Socket socket=null;
private Object writeOutLock= new Object();
private PrintWriter out ;
private final int threadDelay=500;
	public ClientThread(Socket socket){
		this.socket=socket;
	}
	@Override
	public void run(){
	     try {
	            out = new PrintWriter(socket.getOutputStream(), true);
	             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	             String inputLine, outputLine;
	             
	             while ((inputLine = in.readLine()) != null) {
	            	 
	                 outputLine = ServerParser.processInput(inputLine);
	                writeOutSocket(outputLine);
	                if (outputLine.equals("Bye"))
	                     break;
	                 try {
						Thread.sleep(threadDelay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                 
	             }
	             socket.close();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
		
		
	}

	
	public void writeOutSocket(String r){
		 synchronized(writeOutLock){
        	 out.println(r);
         }
	}
}
