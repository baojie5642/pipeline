package WaitNotify;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class NotifyThread implements Runnable {

	private final Object notifyObject;

	private NotifyThread(final Object notifyObject) {
		this.notifyObject = notifyObject;
	}

	public static NotifyThread build(final Object notifyObject) {
		return new NotifyThread(notifyObject);
	}

	@Override
	public void run() {
		myNotify();
	}

	private void myNotify() {
		synchronized (notifyObject) {
			innerSleep(10);
			innerNotify();
			System.out.println("Thread name :" + Thread.currentThread().getName() + ",has bean notifyed. But ……");
			// 下面在直接睡眠五秒，虽然已经执行notify，但是还没有推出同步块，所以另一个线程是不会被唤醒的
			innerSleep(5);
		}
	}

	private void innerNotify() {
		// 在已经获得了监控器的情况下，可以直接下面这样使用
		notifyObject.notify();// 确定被唤醒者的情况下可以使用notify，一般使用notifyall
	}

	private void innerSleep(final int sleepTime) {
		// 为了不去care异常，直接使用locksupport
		LockSupport.parkNanos(TimeUnit.NANOSECONDS.convert(sleepTime, TimeUnit.SECONDS));
	}

}
