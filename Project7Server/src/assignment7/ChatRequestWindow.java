package assignment7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChatRequestWindow extends Application{
	
	private String requestingUser;
	private Stage stage;

	public ChatRequestWindow(String user){
		requestingUser = user;
	}

	@Override
	public void start(Stage primaryStage){
		stage = primaryStage;
		
		BorderPane main = new BorderPane();
		
		Button yes = new Button("YES");

		Button no = new Button("NO");

		Label request = new Label(requestingUser + " Would like to chat");
		
	    yes.setOnAction(e ->{ //if the button is clicked, try to login to the server 
	    	//send yes response to server
	    });
	    
	    no.setOnAction(a ->{
	    	//send no response to server
	    });
		
		main.setCenter(request);
		
		BorderPane.setAlignment(yes, Pos.BOTTOM_RIGHT);
		BorderPane.setMargin(yes,  new Insets(10,10,10,10));
		main.setLeft(yes);
		
		BorderPane.setAlignment(no,  Pos.BOTTOM_LEFT);
		BorderPane.setMargin(no,  new Insets(10,10,10,10));
		main.setRight(no);
		
		Scene scene = new Scene(main, 300, 150);
		
		stage.setScene(scene);
		stage.show();
	}
}
