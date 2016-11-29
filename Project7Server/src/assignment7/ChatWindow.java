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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChatWindow extends Application{
	private int conversationID;
	private Stage mainStage;
	private int messageCount;
	private int userID;
	private PrintWriter out;
	private BufferedReader in;
	private TextArea messages;
	
	public ChatWindow(int conversationID, int userID, PrintWriter out, BufferedReader in){
		this.conversationID = conversationID;
		messageCount = 0;
		this.userID = userID;
		this.out = out;
		this.in = in;
	}
	
	public void start(Stage primaryStage){
		mainStage = primaryStage;
		
		TextField messageField = new TextField(); 
		BorderPane textPane = new BorderPane();
		textPane.setPadding(new Insets(10,10,10,10));
		textPane.setCenter(messageField);
		
		BorderPane mainPane = new BorderPane();
		messages = new TextArea();
		messages.setEditable(false);
		messages.setPrefHeight(450);
		messages.setPrefWidth(500);
		mainPane.setCenter(new ScrollPane(messages));
		mainPane.setBottom(textPane);
		
		Scene scene = new Scene(mainPane, 500, 500);
		mainStage.setScene(scene);
		mainStage.show();
		
		messageField.setOnAction(e -> {
			synchronized(out){
				try{
					String textToSend = messageField.getText();
					out.println("Message goes here");
					out.flush();
				}
				catch (Exception a){
					System.out.println("Error sending Message");
				}
			}
		});
	}
	
	public void updateMessages(String s){
		messages.appendText(s + "\n");
	}
	
	public void quit(){
		mainStage.close();
	}
	
	public int getMessageCount(){
		return messageCount;
	}
	
	public int getConversationID(){
		return conversationID;
	}
}
