package WaitNotify;

public class WaitNotifyMain {

	public static void main(String args[]) {

		final Object object = new Object();

		NotifyThread notifyThread = NotifyThread.build(object);
		WaitThread waitThread = WaitThread.build(object);

		Thread wait = new Thread(waitThread, "wait_thread");
		Thread notify = new Thread(notifyThread, "notify_thread");

		wait.start();
		notify.start();

	}

}
