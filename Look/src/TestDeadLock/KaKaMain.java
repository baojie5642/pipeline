package TestDeadLock;

import TestDeadLock.HaHaLock.InnerLock;

public class KaKaMain {

	public static void main(String args[]){
		//按照固定的规则获取锁就不会出现死锁
		HaHaLock haHaLock=HaHaLock.create();
		
		final Object objectLock=haHaLock.getObjectLock();
		
		final byte[] byteLock=haHaLock.getByteLock();
		
		final InnerLock innerLock =haHaLock.getInnerLock();
		
		A_Thread a_Thread=A_Thread.build(objectLock, byteLock, innerLock);
		B_Thread b_Thread=B_Thread.build(objectLock, byteLock, innerLock);
		C_Thread c_Thread=C_Thread.build(objectLock, byteLock, innerLock);
		
		Thread thread0=new Thread(a_Thread, "i am A_Thread.");
		Thread thread1=new Thread(b_Thread, "i am B_Thread.");
		Thread thread2=new Thread(c_Thread, "i am C_Thread.");
		
		thread0.start();
		thread1.start();
		thread2.start();
	}
}
