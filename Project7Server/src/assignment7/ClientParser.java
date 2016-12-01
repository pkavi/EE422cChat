package assignment7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.stage.Stage;

public class ClientParser {
	
	
	public static String loginRequest_01_Send(String username, String password){
		return "01 "+username.length()+" "+password.length()+" "+username+" "+password;
	}
	
	//if login is a success, set the client's user id
	private static void loginRequest_02_Receive_Success(int userId){
		
		ClientMain.setuserID(userId);
	}
	
	//if login is a failure, then exit the program
	private static void loginRequest_02_Receive_Failure(){
		Platform.runLater(new Runnable(){
			public void run(){
				ClientMain.loginFailure();
			}
		});
		
		
	}
	public static String makeConversation_03_Send(int userId,int chatNumber,ArrayList<Integer> userIds){
		String response="03 "+userId+" "+chatNumber+" "+userIds.size();
		for(Integer u:userIds){
			response=response+" "+u;
		}
		return response;
	}
	//Write handler below
	public static void makeConversation_04_Receive(int userId,int chatNumberGeneratedByUser, int conversationId){
		Platform.runLater(new Runnable(){
			public void run(){
				ClientMain.conversationWindows.get(chatNumberGeneratedByUser).setServerConversationId(conversationId);
				ClientMain.conversationWindows.get(chatNumberGeneratedByUser).start(new Stage());
				
			}
		});
	}
	
	
	public static String getUsersInNetwork_05_Send(int userId){
		return "05 "+userId;
	}
	
	
	//Write handler below
	//Handler is called by 08 or 10 when applicable
	private static void getUsersInNetwork_06_Receive(int userId, HashMap<Integer,String> users){
		Platform.runLater(new Runnable(){
			public void run(){
				ClientMain.setUsernames(users);
			}
		});
		
	}
	
	
	 //conversationsThatNeedUpdate is a hashmap of conversations that need update. keys are conversation ids and values are # of messages in conversation
	public static String updateConversations_07_Send(int userId,int numberOfUsersInNetwork,HashMap<Integer,Integer> conversationsThatNeedUpdate){
		String response="07 "+ userId+" "+numberOfUsersInNetwork+" "+conversationsThatNeedUpdate.size();
		for(Integer i:conversationsThatNeedUpdate.keySet()){
			response=" "+i+" "+conversationsThatNeedUpdate.get(i);
		}
		return response;
	}
	
	//Write handler below
	//newConversationMessages is a hashmap with keys as conversationIds and values as arraylists of new clientMsgs
	private static void updateConversations_08_Receive(int userId,HashMap<Integer,ArrayList<ClientMsg>> newConversationMessages){
		
	}
	public static String getConversationRequests_09_Send(int userId,int numberOfUsersInNetwork){
		return "09 "+userId+" "+numberOfUsersInNetwork;
	}
	private static void getConversationRequests_10_Receive(int userId,HashMap<Integer,ArrayList<Integer>> conversationRequests){
		
	}
	
	
	
	
	public static String sendMessage_15_Send(int userId,int conversationId,int msgId,String timestamp,String msg){
		return "15 "+userId+" "+conversationId+" "+msgId+" "+timestamp.length()+" "+msg.length()+" "+timestamp+" "+msg;
	}
	
	//Write handler below
	private static void sendMessage_16_Receive(int userId,int conversationId,int msgId){
		
	}
	



	
	
	
	public static void parseInput(String input){
		if(input.substring(0,2).equals("02")){//Login request response
			loginRequestParser(input);
		}
		else if(input.substring(0,2).equals("04")){//Initiate conversation request response
			makeConversationParser(input);
		}
		else if(input.substring(0,2).equals("06")){//Get all users in network request response
			getUsersInNetworkParser(input);
		}
		else if(input.substring(0,2).equals("08")){//Conversation update request response
			updateConversationParser(input);
		}
		else if(input.substring(0,2).equals("10")){//Get conversation requests response
			getConversationRequestsParser(input);
		}
		else if(input.substring(0,2).equals("12")){//Get conversations user is part of command response
			//optional
		}
		else if(input.substring(0,2).equals("14")){//Get list of online and offline users for conversations response
			//optional
		}
		else if(input.substring(0,3).equals("16")){//Sent message to server response
			sendMessageParser(input);
		}
		
	}
	private static void loginRequestParser(String input){
		Scanner sc=new Scanner(input);
		sc.nextInt();
		if(sc.nextInt()==1){
			loginRequest_02_Receive_Success(sc.nextInt());
		}
		else{
			loginRequest_02_Receive_Failure();
		}

	}
	private static void getConversationRequestsParser(String input){
		Scanner sc=new Scanner(input);
		sc.nextInt();
		int userId=sc.nextInt();
		int needUpdate=sc.nextInt();
		String rest=sc.nextLine();
		if(needUpdate==1){
		
			//Repeated code below for getting users
				int numUsers=sc.nextInt();
				int tempUserId;
				int tempUserLen;

				String tempUsername;
				HashMap<Integer,String> users=new HashMap<Integer,String>();
				for(int i=0;i<numUsers;i++){
					
					tempUserId=getNextInt(rest);
					rest=discardNextInt(rest);
					
					tempUserLen=getNextInt(rest);
					rest=discardNextInt(rest);
					
					tempUsername=rest.substring(1, 1+tempUserLen);
					
					users.put(tempUserId, tempUsername);
					rest=rest.substring(1+tempUserLen);

				}
				getUsersInNetwork_06_Receive(userId,users);
		}
		//Repeated code above for getting users
		sc=new Scanner(rest);
		int conversationRequestsNum=sc.nextInt();
		HashMap<Integer,ArrayList<Integer>> conversationRequests=new HashMap<Integer,ArrayList<Integer>>();
		for(int i=0;i<conversationRequestsNum;i++){
			int conversationId=sc.nextInt();
			ArrayList<Integer> usersInConvo=new ArrayList<Integer>();
			int usersInConvoNum=sc.nextInt();
			for(int u=0;u<usersInConvoNum;u++){
				usersInConvo.add(sc.nextInt());
			}
			conversationRequests.put(conversationId, usersInConvo);
		}
		
		getConversationRequests_10_Receive(userId,conversationRequests);
			
		
		
	}
	private static void sendMessageParser(String input){
		Scanner sc=new Scanner(input);
		sc.nextInt();
		int userId=sc.nextInt();
		int conversationId=sc.nextInt();
		int msgId=sc.nextInt();
		sendMessage_16_Receive(userId,conversationId,msgId);
	}
	private static void updateConversationParser(String input){
		Scanner sc=new Scanner(input);
		sc.nextInt();
		int userId=sc.nextInt();
		int needUpdate=sc.nextInt();
		String rest=sc.nextLine();
		if(needUpdate==1){
		
			//Repeated code below for getting users
				int numUsers=sc.nextInt();
				int tempUserId;
				int tempUserLen;

				String tempUsername;
				HashMap<Integer,String> users=new HashMap<Integer,String>();
				for(int i=0;i<numUsers;i++){
					
					tempUserId=getNextInt(rest);
					rest=discardNextInt(rest);
					
					tempUserLen=getNextInt(rest);
					rest=discardNextInt(rest);
					
					tempUsername=rest.substring(1, 1+tempUserLen);
					
					users.put(tempUserId, tempUsername);
					rest=rest.substring(1+tempUserLen);

				}
				getUsersInNetwork_06_Receive(userId,users);
		}
			//Repeated code above for getting users
		
		int convosToUpdate=getNextInt(rest);
		rest=discardNextInt(rest);
		HashMap<Integer,ArrayList<ClientMsg>> conversationMessages=new HashMap<Integer,ArrayList<ClientMsg>>();
		for(int i=0;i<convosToUpdate;i++){
			int conversationId=getNextInt(rest);
			rest=discardNextInt(rest);
			
			
			int numNewMessages=getNextInt(rest);
			rest=discardNextInt(rest);
			
			ArrayList<ClientMsg> newM=new ArrayList<ClientMsg>();
			
			for(int p=0;p<numNewMessages;p++){
				Scanner scan=new Scanner(rest);
				int userIdSent=scan.nextInt();
				int timestampLen=scan.nextInt();
				int msgLen=scan.nextInt();
				rest=scan.nextLine();
				
				String timestamp=rest.substring(1,1+timestampLen);
				String msg=rest.substring(2+timestampLen,2+timestampLen+msgLen);
				rest=rest.substring(2+timestampLen+msgLen);
				newM.add(new ClientMsg(userIdSent,timestamp,msg));
			}
			conversationMessages.put(conversationId, newM);
		}
		
		updateConversations_08_Receive(userId,conversationMessages);
		
		
		
	}
	private static void makeConversationParser(String input){
		Scanner sc=new Scanner(input);
		sc.nextInt();
		int userId=sc.nextInt();
		int chatNumberGeneratedByUser=sc.nextInt();
		int conversationId=sc.nextInt();
		makeConversation_04_Receive(userId,chatNumberGeneratedByUser,conversationId);
	}
	
	
	
	private static void getUsersInNetworkParser(String input){
		Scanner st=new Scanner(input);
		st.nextInt();
		int userId=st.nextInt();
		
		//Repeated code below for getting users
		int numUsers=st.nextInt();
		int tempUserId;
		int tempUserLen;
		String rest=st.nextLine();
		String tempUsername;
		HashMap<Integer,String> users=new HashMap<Integer,String>();
		for(int i=0;i<numUsers;i++){
			
			tempUserId=getNextInt(rest);
			rest=discardNextInt(rest);
			
			tempUserLen=getNextInt(rest);
			rest=discardNextInt(rest);
			
			tempUsername=rest.substring(1, 1+tempUserLen);
			
			users.put(tempUserId, tempUsername);
			rest=rest.substring(1+tempUserLen);

		}
	//Repeated code above for getting users
	
		getUsersInNetwork_06_Receive(userId,users);
		
	}
	
	private static String discardNextInt(String in){
		int spotAfterInt=in.length();
		boolean started=false;
		for(int i=0;i<in.length();i++){
			if(in.charAt(i)>=48 && in.charAt(i)<=57 && !started){
				started=true;
			}
			else if((in.charAt(i)<48 || in.charAt(i)>57)&& started){
				spotAfterInt=i;
				return in.substring(i);
			}

		}
		return "";
	}
	private static int getNextInt(String in){
		boolean started=false;
		int spotAfterInt=in.length();
		int startInt=0;
		for(int i=0;i<in.length();i++){
			if(in.charAt(i)>=48 && in.charAt(i)<=57 && !started){
				started=true;
				startInt=i;
			}
			else if((in.charAt(i)<48 || in.charAt(i)>57) && started){
				spotAfterInt=i;
				break;
			}
		}
		return Integer.parseInt(in.substring(startInt,spotAfterInt));
	}
}
