package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer {
	static HashMap<Integer, UserEntry> loginInfo=new HashMap<Integer,UserEntry>();
	 static int globalUserId=0;
	public static Object lockLoginInfo= new Object();//Locked whenever ids or loginInfo is written to
	
	
	
	
	
	static HashMap<Integer,Conversation> conversations=new  HashMap<Integer,Conversation>();
	 static int conversationId=0;
	 static Object lockConversations= new Object();//Locked whenever ids or conversations are written to
	


	
	
	public static void main(String[] args){
		int portNumber = Integer.parseInt(args[0]);
		
		   try {
		            ServerSocket serverSocket = new ServerSocket(portNumber);
		            System.out.println("Running on port " + portNumber );
		            while(true){
		            	
		            	new Thread(new ClientThread(serverSocket.accept())).start();
		            	try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
		            }
		   }
		   catch (IOException e) {
               System.err.println("Could not listen on port " + portNumber);
               System.exit(-1);
           }

		          
	}
	
	/**
	 * @param username username to check
	 * @param password password to check 
	 * @return true if new user is created or username password exists; false if user exists but password does not match
	 * returns the userId of user
	 */
	public static String validLogin(String username, String password){
		synchronized(lockLoginInfo){
			for(UserEntry e: loginInfo.values()){
				if(e.getUsername().equals(username)){
					if(e.getPassword().equals(password)){
						return e.getUserId()+"";
					}else{
						return null;
					}
				}
			}
			UserEntry newUser=new UserEntry(username,password,globalUserId);
			
			loginInfo.put(globalUserId, newUser);
			globalUserId++;
			return (globalUserId-1)+"";
		}
		
		
	}
	
	public static UserEntry getUserEntryFromUsername(String username){
		synchronized(lockLoginInfo){
			for(UserEntry e: loginInfo.values()){
				if(e.getUsername().equals(username)){
					return e;
				}
			
			}
		}
		return null;
	}
	public static void addConversation(Conversation e){
		synchronized(lockConversations){
			conversations.put(e.getConversationId(),e );
		}
	}
	public static int numUsers(){
		synchronized(lockLoginInfo){
			return loginInfo.size();
		}
		
	}
	
	public static String usersString(){
		String usersString;
		synchronized(lockLoginInfo){
			usersString=" "+loginInfo.size();
			for(UserEntry e:loginInfo.values()){
				usersString=usersString+" "+e.getUserId()+ " " + e.getUsername().length()+ " "+ e.getUsername();
			}
		}
		return usersString;
	}
	
	
}
