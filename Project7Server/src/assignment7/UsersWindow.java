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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UsersWindow extends Application{

	HashMap<Integer, String> users;
	ArrayList<CheckBox> userChoices;
	Stage mainStage;
	GridPane usersPane;
	PrintWriter out;
	
	public UsersWindow(HashMap<Integer, String> users, PrintWriter out){
		userChoices = new ArrayList<CheckBox>();
		this.users = users;
		this.out = out;
	}
	
	public UsersWindow(PrintWriter out){
		userChoices = new ArrayList<CheckBox>();
		this.users = new HashMap<Integer, String>();
		this.out = out;
	}
	
	@Override
	public void start(Stage primaryStage){
		mainStage = primaryStage;
		
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10,10,10,10));

		usersPane = new GridPane();
		usersPane.setVgap(5);
		usersPane.setHgap(10);
		
		updateChoiceBoxes();
		
		BorderPane buttonPane = new BorderPane();
		buttonPane.setPadding(new Insets(5,5,5,5));
		
		Button request = new Button("Send Chat Request");
		
	    request.setOnAction(e ->{ //if the button is clicked, try to login to the server 
	    	try{
		    	ArrayList<String> selectedUsers = getSelectedBoxes();
		    	String users = "";
		    	for(String s:selectedUsers)
		    		users+=s + " ";
		    	out.println(users);
		    	out.flush();
		    	System.out.println(users);
	    	}
	    	catch(Exception a){
	    		a.printStackTrace();
	    	}
	    });
		buttonPane.setCenter(request);		
		
		mainPane.setCenter(usersPane);
		mainPane.setBottom(buttonPane);
		
		Scene scene = new Scene(mainPane, 200, 300);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public void updateUsers(HashMap<Integer, String> users){
		this.users = users;
		updateChoiceBoxes();
	}
	
	private void updateChoiceBoxes(){
		for(String username: users.values()){
			CheckBox c = new CheckBox(username);
			userChoices.add(c);
		}
		usersPane.getChildren().clear();
		int i = 0;
		for(CheckBox c: userChoices){
			usersPane.add(c, 0, i);
			i++;
		}
	}
	
	private ArrayList<String> getSelectedBoxes(){
		ArrayList<String> selectedBoxes = new ArrayList<String>();
		for(CheckBox c: userChoices){
			if(c.isSelected())
				selectedBoxes.add(c.getText());
		}
		return selectedBoxes;
	}
	
}
