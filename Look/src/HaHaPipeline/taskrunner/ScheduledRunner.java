package HaHaPipeline.taskrunner;

import java.util.concurrent.Semaphore;

import AutoMachine.A_AutoMachine;
import HaHaPipeline.WriteLog;
import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.task.Task;

public class ScheduledRunner implements Runnable {

	private final Task task;

	private ScheduledRunner(final Task task) {
		this.task = task;
	}

	public static ScheduledRunner create(final Task task) {
		return new ScheduledRunner(task);
	}

	@Override
	public void run() {
		int howManyLine = -1;
		howManyLine = task.getHowManyLineInTask();
		work(howManyLine);
	}

	private void work(final int howManyLine) {
		putLineIntoAutoMachine(howManyLine);
		waitForTaskFinish(howManyLine);
		checkAvailablePermits();
	}

	private void putLineIntoAutoMachine(final int howManyLine) {
		OneLineInTask oneLineInTask = null;
		for (int i = 0; i < howManyLine; i++) {
			oneLineInTask = task.getAllLines().get(i);
			A_AutoMachine.putOneLine(oneLineInTask);
		}
	}

	private void waitForTaskFinish(final int howManyLine) {
		for (int i = 0; i < howManyLine; i++) {
			task.acquireOneSemaphore();
		}
	}

	private void checkAvailablePermits() {
		final Semaphore semaphore = task.getTaskSemaphore();
		if (0 == semaphore.availablePermits()) {
			return;
		} else {
			//检测提前释放信号量的情况，waitForTaskFinish方法不过，是不会运行到此处的，如果进入次分支说明出错的，请查看文件
			WriteLog.putErrorLog(Thread.currentThread().getName()+":semaphore != 0 , do not know why ?");
		}
	}

}
