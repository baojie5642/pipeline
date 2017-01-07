package nettytest;

import java.util.concurrent.TimeUnit;

import org.springframework.jdbc.support.incrementer.AbstractIdentityColumnMaxValueIncrementer;

public class MyTask implements Runnable{
	
	private final long messageID;
	private final String request;
	
	
	private MyTask(final long messageID,final String request) {
		this.messageID=messageID;
		this.request=request;
	}
	
	public static MyTask create(final long messageID,final String request) {
		MyTask myTask =new MyTask(messageID, request);
		return myTask;
	}
	
	@Override 
	public void run(){
		try {
			System.out.println("start some work :"+request);
			TimeUnit.MILLISECONDS.sleep(6);
			System.out.println("finish some work :"+request);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MyChannel myChannel=null;
		try{
			myChannel=MyCache.MyCache.get(messageID);
		}catch (Throwable e) {
			assert true;
		}
		if(null==myChannel){
			return;
		}else {
			myChannel.weakUpIfWait("i have done.");
		}
	}
	
}
