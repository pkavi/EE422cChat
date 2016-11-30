package assignment7;

import java.util.HashMap;

public class ConversationsMonitor implements Runnable {

	private static final int threadDelay=2500;
	private static final long maxDelayBeforeMarkedInactive=3000;
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(threadDelay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HashMap<Integer,Conversation> convos=MainServer.conversationsCopy();
			for(Conversation c:convos.values()){
				c.setFlagForActiveConversation(maxDelayBeforeMarkedInactive);
			}
		}
		
	}

}
