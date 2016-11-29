/* CHATROOM ClientMain.java
 * EE422C Project 7 submission by 
 * Sriram Ravula
 * sr39533
 * 16475
 * Pranav Kavikondala
 * pk6994
 * 16470
 * Slip days used: 1
 * Git URL: https:https://github.com/pkavi/EE422cChat.git
 * Fall 2016
 */

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

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientMain extends Application{
	int port;
	String serverIP;
	BufferedReader in;
	PrintWriter out;
	Socket socket;
	String username;
	String password;
	int userID;
	HashMap<Integer, Stage> conversations; //tracks the conversation ID and stage for each conversation
	HashMap<Integer, Integer> messagesPerConversation; //tracks the number of messages for each conversation - useful for 
    boolean runSocket = false;

	public void start(Stage primaryStage){
		Button submit = new Button("Submit");
	    TextField ip = new TextField();
	    TextField portNum = new TextField();
	    GridPane ipGrid = new GridPane();
	    
	    ipGrid.setPadding(new Insets(10,10,10,10));
	    ipGrid.setVgap(10);
	    ipGrid.setHgap(10);
	    
	    ip.setText("Server IP");
	    portNum.setText("Server port");
	    
		Button submitLogin = new Button("Login");
	    TextField usernameInput = new TextField();
	    TextField passwordInput = new TextField();
	    
	    usernameInput.setText("Username");
	    passwordInput.setText("Password");
	    
	    submitLogin.setOnAction(e ->{ //if the button is clicked, try to login to the server 
	    	if(runSocket){
		    	try{
		    		username = usernameInput.getText();
		    		password = passwordInput.getText();
		    		
					out.println("01 " + username.length() + " " + password.length() + " " + username + " " + password);
					out.flush();
		    		
					System.out.println(in.readLine());
					
					submitLogin.setDisable(true);
		    	}
		    	catch(Exception a){
		    		System.out.println("User Login Error");
		    	}
	    	}
	    });
	    
	    submit.setOnAction(e ->{ //if the button is clicked, try to connect to a server using the current values in the text fields
	    	try{
	    		serverIP = ip.getText();
	    		port = Integer.parseInt(portNum.getText());
	    		runSocket = true;
	    		
				// Create a socket to connect to the server 
				@SuppressWarnings("resource")
				Socket socket = new Socket(serverIP, port); 

				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		        out = new PrintWriter(socket.getOutputStream(), true);
				
				System.out.println("SUCCESSS");
				
				submit.setDisable(true);
	    	}
	    	catch(Exception a){
	    		System.out.println("IP parsing error");
	    	}
	    });
	    
	    GridPane.setConstraints(ip, 0, 0);
	    GridPane.setConstraints(portNum, 0, 1);
	    GridPane.setConstraints(submit, 0, 2);
	    GridPane.setConstraints(usernameInput, 0, 4);
	    GridPane.setConstraints(passwordInput, 0, 5);
	    GridPane.setConstraints(submitLogin, 0, 6);
	    
	    ipGrid.getChildren().addAll(ip, portNum, submit, submitLogin, passwordInput, usernameInput);
	    
	    Scene scene = new Scene(ipGrid);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	    ChatWindow chat = new ChatWindow(1, userID, out, in);
	    chat.start(new Stage());
	}
	
	public void usersWindow(){
		
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
