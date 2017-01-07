package TestDeadLock;

public class HaHaLock {

	
	private HaHaLock(){
		
	}
	
	public static HaHaLock create(){
		return new HaHaLock();
	}
	//锁的三种创建形式
	private final byte[] byteLock=new byte[0];
	
	private final Object objectLock=new Object();
	
	private final InnerLock innerLock=new InnerLock();
	
	
	//一般内部使用会写成private
	public final class InnerLock{
		
	}

	public byte[] getByteLock() {
		return byteLock;
	}

	public Object getObjectLock() {
		return objectLock;
	}

	public InnerLock getInnerLock() {
		return innerLock;
	}
	
}
