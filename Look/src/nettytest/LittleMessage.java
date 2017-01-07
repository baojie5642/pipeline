package nettytest;

import java.io.Serializable;

public class LittleMessage  implements Serializable{
	private static final long serialVersionUID = 2016112513225403285L;
	
	private final long messageID;
	
	private String request;
	
	private String reponse;
	
	public LittleMessage(final long messageID){
		this.messageID=messageID;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getReponse() {
		return reponse;
	}

	public void setReponse(String reponse) {
		this.reponse = reponse;
	}

	public long getMessageID() {
		return messageID;
	}
	
}
