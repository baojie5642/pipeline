package HaHaPipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

//这个类是一次性使用的，需要循环使用，需要自己定制
public class WriteLog implements Runnable {

	private static final LinkedBlockingQueue<String> ErrorLinesQueue = new LinkedBlockingQueue<>(9999);

	private static final AtomicBoolean buttonOn = new AtomicBoolean(true);

	private final AtomicBoolean hasBreakRetry = new AtomicBoolean(false);

	private final FileOutputStream fileOutputStream;

	public static final String Line_Separator;

	private final Object lock = new Object();

	private final File errorFile;

	static {
		// avoid security issues
		final StringBuilderWriter buf = new StringBuilderWriter(4);
		final PrintWriter out = new PrintWriter(buf);
		out.println();
		Line_Separator = buf.toString();
		out.close();
	}

	public static WriteLog create(final File errorFile) {
		return new WriteLog(errorFile);
	}

	private WriteLog(final File errorFile) {
		this.errorFile = errorFile;
		this.fileOutputStream = buildFileOutputStream(errorFile, true);
	}

	private FileOutputStream buildFileOutputStream(final File file, final boolean append) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = BuildStream.openOutputStream(file, append);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CheckNull.checkNull(fileOutputStream);
		return fileOutputStream;
	}

	@Override
	public void run() {
		addHookThread();
		String line = null;
		while (buttonOn.get()) {
			line = getLine() + Line_Separator;
			try {
				fileOutputStream.write(line.getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		hasBreak();
	}

	private void addHookThread() {
		System.out.println("进入钩子构造");
		final CloseStreamHook closeStreamHook = new CloseStreamHook();
		final Thread hookThread = new Thread(closeStreamHook, "I am close streams hook.");
		Runtime.getRuntime().addShutdownHook(hookThread);
		System.out.println("退出钩子构造");
	}

	private String getLine() {
		String line = null;
		try {
			line = ErrorLinesQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return line;
	}

	private void hasBreak() {
		synchronized (hasBreakRetry) {
			hasBreakRetry.set(true);
			hasBreakRetry.notify();
		}
	}

	public static boolean putErrorLog(final String errorLog) {
		if (buttonOn.get()) {
			realPut(errorLog);
			return true;
		} else {
			return false;
		}
	}

	private static void realPut(final String errorLog) {
		try {
			ErrorLinesQueue.put(errorLog);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		turnOff();
		waitWhenClose();
		finishClose();
	}

	private void turnOff() {
		turnOffButton();
		// 放入一颗毒丸，这里使用的是毒丸的方式结束线程，还有其他方式
		// 基本模式请参考两阶段中止
		putPoisonPill();
	}

	private void finishClose() {
		cleanQueue();
		closeFileOutputStream();
	}

	private void turnOffButton() {
		synchronized (lock) {
			if (buttonOn.get()) {
				buttonOn.set(false);
			} else {
				return;
			}
		}
	}

	private void putPoisonPill() {
		if (hasBreakRetry.get()) {
			return;
		} else {
			realPut("Close this writer.");
		}
	}

	private void waitWhenClose() {
		synchronized (hasBreakRetry) {
			while (hasBreakRetry.get() == false) {
				try {
					hasBreakRetry.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void cleanQueue() {
		ErrorLinesQueue.clear();
	}

	private void closeFileOutputStream() {
		if (null != fileOutputStream) {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private final class CloseStreamHook implements Runnable {
		public CloseStreamHook() {

		}

		@Override
		public void run() {
			close();
		}
	}

	public static String getLineSeparator() {
		return Line_Separator;
	}

	public static LinkedBlockingQueue<String> getErrorlinesqueue() {
		return ErrorLinesQueue;
	}

	public File getErrorFile() {
		return errorFile;
	}

}
