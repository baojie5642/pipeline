package WaitNotify;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class WaitThread implements Runnable {

	private final Object waitObject;

	private WaitThread(final Object waitObject) {
		this.waitObject = waitObject;
	}

	public static WaitThread build(final Object waitObject) {
		return new WaitThread(waitObject);
	}

	@Override
	public void run() {
		myWait();
	}

	private void myWait() {
		synchronized (waitObject) {
			innerSleep(1);
			System.out.println("Thread name :" + Thread.currentThread().getName() + ", wait begin .");
			innerWait();
			System.out.println("Thread name :" + Thread.currentThread().getName() + ", wait finish .");
			innerSleep(1);
		}
	}

	private void innerWait() {
		try {
			waitObject.wait();
		} catch (InterruptedException e) {
			assert true;// ignore
		}
	}

	private void innerSleep(final int sleepTime) {
		// 为了不去care异常，直接使用locksupport
		LockSupport.parkNanos(TimeUnit.NANOSECONDS.convert(sleepTime, TimeUnit.SECONDS));
	}

}
