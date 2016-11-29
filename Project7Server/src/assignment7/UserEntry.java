package assignment7;

public class UserEntry {
	private String username=null;
	private String password=null;
	private int userId;
	public UserEntry(String username,String password,int userId){
		this.username=username;
		this.password=password;
		this.userId=userId;
	}
	public String getUsername(){
		return username;
	}
	public int getUserId(){
		return userId;
		
	}
	public String getPassword(){
		return password;
	}

	
	public void setConnectedClient(){
		
	}
}
