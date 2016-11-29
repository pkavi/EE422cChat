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
	            	 
	                 outputLine = processInput(inputLine);
	                writeOutSocket(outputLine);
	                 try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                 if (outputLine.equals("Bye"))
	                     break;
	             }
	             socket.close();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
		
		
	}

	public String processInput(String input){
		if(input.substring(0,2).equals("01")){//Login request
			return requestToLogin(input);
		}
		else if(input.substring(0,2).equals("03")){//Initiate conversation request
			return requestToStartConversation(input);
		}
		else if(input.substring(0,2).equals("07")){//Conversation request returned back from client
			 
		}
		return null;
	}
	//Will be as follows: 01 <Username length> <Password length> <username> <password>
	public String requestToLogin(String input){
		Scanner st=new Scanner(input);
		st.nextInt();
		int usernameLen=st.nextInt();
		int passwordLen=st.nextInt();
		String restOfMsg=st.nextLine().substring(1);
		String username=restOfMsg.substring(0,usernameLen);
		String password=restOfMsg.substring(usernameLen+2);
		String response=MainServer.validLogin(username,password);
		if(response==null){
			return "02 0 ";
		}

		return "02 1 "+response;
		
	}
	//Method to start new conversation
	// 03 <userIdSource> <chatNumberGeneratedBySource> <numClientsToConnectTo> <client1Id> [other clientId seperated by whitespace]
	public String requestToStartConversation(String input){
		Scanner st=new Scanner(input);
		st.nextInt();
		int initiatorUserId=st.nextInt();
		int chatNumberSource=st.nextInt();
		int numClientsToConnectTo=st.nextInt();
		Conversation newConvo=new Conversation();
		
		newConvo.addParticipant(MainServer.loginInfo.get(initiatorUserId));
		
		for(int i=0;i<numClientsToConnectTo;i++){
			newConvo.addParticipant(MainServer.loginInfo.get(st.nextInt()));
			
		}
		
		
		
		
		newConvo.setUserIdActive(initiatorUserId);
		
		newConvo.setUserIdRequestSent(initiatorUserId);
		
			MainServer.addConversation(newConvo);
		
		return "06 "+initiatorUserId+" "+chatNumberSource+" "+newConvo.getConversationId();
		
	}
	public String pollingRequest(String input){
		String resultString="08";
		Scanner st=new Scanner(input);
		st.nextInt();
		int initiatorUserId=st.nextInt();
		int clientUsersKnown=st.nextInt();
		int clientConversationsKnown=st.nextInt();
		int clientConversationsNeedUpdate=st.nextInt();
		if(clientUsersKnown!=-1 && MainServer.numUsers()!=clientUsersKnown){
		
				resultString=resultString+" 1"+ MainServer.usersString();

		}
		else{
			resultString=resultString+" 0";
		}
		
		
		resultString=resultString+" 0";//Functionality for clientConversationKnown not implemented
		
		//Client conversations that need update
		if(clientConversationsNeedUpdate!=-1 && clientConversationsNeedUpdate!=0){
			resultString=resultString+ " " +clientConversationsNeedUpdate;
			for(int i=0;i<clientConversationsNeedUpdate;i++){
				int conversationId=st.nextInt();
				int numberOfMessagesInConversationKnown=st.nextInt();
				Conversation convo;
				synchronized(MainServer.lockConversations){
					convo=MainServer.conversations.get(conversationId);
				}
				resultString=resultString+" "+conversationId+convo.getNewMessages(numberOfMessagesInConversationKnown);
			}
		}else{
			resultString=resultString + " 0";
		}
		
		//Conversation requests flag check
		if(st.nextInt()==1){
			
		}
		else{
			resultString=resultString+" 0";
		}
		
		
		
		return null;
	}
	public void writeOutSocket(String r){
		 synchronized(writeOutLock){
        	 out.println(r);
         }
	}
}
