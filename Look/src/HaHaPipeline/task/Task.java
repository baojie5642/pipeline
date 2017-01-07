package HaHaPipeline.task;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import HaHaPipeline.MakeRandomNum;
import HaHaPipeline.WriteLog;

public class Task {

	// 总的任务最高层，也是最外一层的抽象

	// 每一个taskline不一定要有全部的abcd部分，由用户设置
	// 每一个task也不一定就是3条line，随便几条都可以，由用户设置

	/*
	 *               |---------taskline(part:ABCD)--------| 
	 *               |                                    |
	 *               |                                    | 
	 * task(start)---|---------taskline(part:ABCD)--------|---task(finished) 
	 *               |                                    |
	 *               |                                    | 
	 *               |---------taskline(part:ABCD)--------|
	 * 
	 */

	private final int waitSecondOnlyBeforeFirstStart;

	private final ArrayList<OneLineInTask> allLines;

	private final int delaySecondAfterFinish;

	private final Semaphore taskSemaphore;

	private final int howManyLineInTask;

	private final String taskName;

	private final long taskID;

	public static Task create(final String taskName, final long taskID, final int howManyLineInTask,
			final ArrayList<OneLineInTask> allLines, final Semaphore taskSemaphore) {
		return new Task(taskName, taskID, howManyLineInTask, allLines, taskSemaphore);
	}

	private Task(final String taskName, final long taskID, final int howManyLineInTask,
			final ArrayList<OneLineInTask> allLines, final Semaphore taskSemaphore) {
		this.taskName = taskName;
		this.taskID = taskID;
		this.howManyLineInTask = howManyLineInTask;
		this.allLines = allLines;
		this.waitSecondOnlyBeforeFirstStart = MakeRandomNum.randomNumAddOne(60);
		this.delaySecondAfterFinish = MakeRandomNum.randomNumAddOne(180);
		this.taskSemaphore = taskSemaphore;
		buildSemaphore();
	}

	//这个也可以方法到工具类里面，但是后来想想还是算了
	private void buildSemaphore() {
		checkAvailablePermits();
	}

	private void checkAvailablePermits(){
		final int howManySemaphoreCanUse=taskSemaphore.availablePermits();
		if(howManySemaphoreCanUse==howManyLineInTask){
			acquireAllSem();
		}else{
			//检测初始化出错的情况，几乎不会出错
			WriteLog.putErrorLog("semaphore availablePermits is on error.in Task.class");
			throw new IllegalStateException("semaphore availablePermits is on error.");
		}
	}
	
	private void acquireAllSem() {
		try {
			taskSemaphore.acquire(howManyLineInTask);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("init semaphore occur error.please check it.");
		}
	}
	
	public void acquireOneSemaphore(){
		try {
			taskSemaphore.acquire(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getHowManyLineInTask() {
		return howManyLineInTask;
	}

	public ArrayList<OneLineInTask> getAllLines() {
		return allLines;
	}

	public Semaphore getTaskSemaphore() {
		return taskSemaphore;
	}

	public String getTaskName() {
		return taskName;
	}

	public long getTaskID() {
		return taskID;
	}

	public int getWaitSecondOnlyBeforeFirstStart() {
		return waitSecondOnlyBeforeFirstStart;
	}

	public int getDelaySecondAfterFinish() {
		return delaySecondAfterFinish;
	}

}
