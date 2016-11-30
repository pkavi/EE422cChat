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

import java.util.*;

import java.io.BufferedReader;
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
	static int port = -1;
	static String serverIP = "";
	static BufferedReader in = null;
	static PrintWriter out = null;
	static Socket socket = null;
	static String username = "";
	static String password = "";
	static int userID = -1;
	static HashMap<Integer, ChatWindow> conversations = new HashMap<Integer, ChatWindow>(); //tracks the conversation ID and stage for each conversation
	static HashMap<Integer, Integer> messagesPerConversation = new HashMap<Integer, Integer>(); //tracks the number of messages for each conversation - useful for 
	static HashMap<Integer, String> usernames = new HashMap<Integer, String>(); //contains the user IDs and corresponding usernames
	static boolean runSocket = false;
    UsersWindow u;

	public void start(Stage primaryStage){
		Button submit = new Button("Connect");
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
				
			    HashMap<Integer, String> a = new HashMap<Integer, String>();
			    a.put(1, "a");
			    a.put(2,  "b");
			    u = new UsersWindow(a, out);
			    u.start(new Stage());
				
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
	    
	    //Thread polling = new Thread((Runnable)new PollingThread());
	    //polling.start();
	    
	    ChatRequestWindow kek = new ChatRequestWindow("kek");
	    kek.start(new Stage());
	}
	
	public static BufferedReader getServerInput(){
		return in;
	}
	
	public static PrintWriter getClientOutput(){
		return out;
	}
	
	public static String getUsername(){
		return username;
	}
	
	public static int getUserID(){
		return userID;
	}
	
	public static HashMap<Integer, Integer> getMessagesPerConversation(){
		return messagesPerConversation;
	}
	
	public static void updateMessagesPerConversation(Integer conversationID, Integer numMessages){
		messagesPerConversation.put(conversationID, numMessages);
	}
	
	public static void addNewConversation(Integer conversationID, Integer numMessages){
		messagesPerConversation.put(conversationID, numMessages);
	}
	
	public static HashMap<Integer, String> getUsernames(){
		return usernames;
	}
	
	public static void setUsernames(HashMap<Integer, String> updated){
		usernames = new HashMap<Integer, String>(updated);
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
