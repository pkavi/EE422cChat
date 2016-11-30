package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PollingThread implements Runnable{
	
	private BufferedReader in = ClientMain.getServerInput();
	private PrintWriter out = ClientMain.getClientOutput();

	@Override
	public void run() {
		while(true){
			synchronized(out){
				String serverOutput = "";
				try {
					while ((serverOutput = in.readLine()) != null) {
					    ClientParser.parseInput(serverOutput);
					}
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(500);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
