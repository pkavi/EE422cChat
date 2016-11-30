package assignment7;

import java.util.ArrayList;
import java.util.HashMap;

public class Conversation {
	private static final int maxDelayToAccept=4000;
	
	private HashMap<Integer,UserEntry> participants=new HashMap<Integer,UserEntry>();
	private Object lockParticipants=new Object();
	
	
	
	private HashMap<Integer,Boolean> activeParticipants=new HashMap<Integer,Boolean>();
	private HashMap<Integer,Long> lastUpdated=new HashMap<Integer,Long>();
	private Object lockActiveParticipants=new Object(); //Lock applies to lastUpdated as well
	
	
	private HashMap<Integer,Boolean> requestSent=new HashMap<Integer,Boolean>();
	private Object lockRequestSent=new Object();
	
	
	
	private ArrayList<Message> messages=new ArrayList<Message>();
	private Object lockMessages=new Object();
	
	private int conversationId;
	
	
	
	
	
	public Conversation(){
		conversationId=MainServer.getNewConversationId();
		
	}
	
	
	public void addParticipant(UserEntry e){
		synchronized(lockParticipants){
		participants.put(e.getUserId(),e);
		}
		synchronized(lockActiveParticipants){
		activeParticipants.put(e.getUserId(),false);
		lastUpdated.put(e.getUserId(), null);
		}
		synchronized(lockRequestSent){
			requestSent.put(e.getUserId(), false);
		}
	}
	public int getConversationId(){
		return conversationId;
	}
	
	//Phase 1 after making connection 
	public void setUserIdActive(int initiatorUserId){
		synchronized(lockActiveParticipants){
			lastUpdated.put(initiatorUserId,System.currentTimeMillis());
			activeParticipants.put(initiatorUserId,true);
		}

	}
	
	public void setUserIdRequestSent(int initiatorUserId){
		synchronized(lockRequestSent){
			requestSent.put(initiatorUserId, true);
		}
	}
	
	public String getNewMessages(int messagesClientHas){
		String returnStr="";
		synchronized(lockMessages){
			if(messagesClientHas<messages.size()){
				returnStr=returnStr+" "+(messages.size()-messagesClientHas);
				for(int i=messagesClientHas;i<messages.size();i++){
					int sender=messages.get(i).getSender();
					String timestamp=messages.get(i).getTimestamp();
					String msg=messages.get(i).getMsg();
					returnStr=returnStr+" "+sender+" "+timestamp.length()+" "+msg.length()+" "+timestamp+" "+msg; 
				}
			}
			else{
				returnStr=returnStr+" 0";
			}
		}
		return returnStr;
	}
	public String getActiveInactiveUsers(){
		String returnStr="";
	
		synchronized(lockActiveParticipants){
			returnStr=returnStr+" "+activeParticipants.size();
			for(Integer id:activeParticipants.keySet()){
				returnStr=returnStr+" "+id+" "+convertBooleanToInt(activeParticipants.get(id));
			}
		}
		return returnStr;
	}
	
	public void addMsgToConversation(int userId, String timestamp, String msg){
		Message a=new Message(userId,timestamp,msg);
		synchronized(lockMessages){
			messages.add(a);
		}
		synchronized(lockActiveParticipants){
			lastUpdated.put(userId, System.currentTimeMillis());
			activeParticipants.put(userId, true);
		}
	}
	
	public boolean containsUser(int userId){
		synchronized(lockParticipants){
			for(Integer i:participants.keySet()){
				if(i==userId){
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean getRequestedForUserAndSet(int userId){//Sets the request to serviced if false
		boolean ret=true;
		synchronized(lockRequestSent){
			ret=requestSent.get(userId);
		}
		if(ret==false){
			setUserIdRequestSent(userId);
			return false;
		}
		return true;
		
	}
	public int convertBooleanToInt(boolean s){
		if(s){
			return 1;
		}
		return 0;
	}
	
	
	
	public String getUsersInConversation(){
		String res;
		synchronized(lockParticipants){
			res=" "+participants.size();
			for(Integer i:participants.keySet()){
				res=res+" "+i;
			}
		}
		return res;
		
		
	}
	public void setFlagForActiveConversation(long maxDelay){
		synchronized(lockActiveParticipants){
			for(Integer i:activeParticipants.keySet()){
				if(Math.abs(lastUpdated.get(i)-System.currentTimeMillis())<maxDelay){
					activeParticipants.put(i, true);
				}else{
					activeParticipants.put(i, false);
				}
			}
		}
	}
	
	
}
