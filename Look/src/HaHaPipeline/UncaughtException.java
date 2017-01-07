package HaHaPipeline;

import java.lang.Thread.UncaughtExceptionHandler;

public class UncaughtException implements UncaughtExceptionHandler {

	public void uncaughtException(final Thread t, final Throwable e) {
		innerCheck(t, e);
		hahaInterrupted(t);
	}

	private void innerCheck(final Thread t, final Throwable e) {
		CheckNull.checkNull(t);
		CheckNull.checkNull(e);
	}

	private void hahaInterrupted(final Thread t) {
		try {
			t.interrupt();
		} finally {
			alwaysInterrupt(t);
		}
	}

	private void alwaysInterrupt(final Thread t) {
		if (!t.isInterrupted()) {
			t.interrupt();
		}
		if (t.isAlive()) {
			t.interrupt();
		}
	}

}