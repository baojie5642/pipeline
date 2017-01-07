package AutoMachine;

import java.util.concurrent.LinkedBlockingQueue;

import HaHaPipeline.CheckNull;
import HaHaPipeline.CheckPart;
import HaHaPipeline.GetPartFromLine;
import HaHaPipeline.WorkThreadPool;
import HaHaPipeline.WriteLog;
import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.task.parttask.C_PartOfTask;
import HaHaPipeline.taskrunner.C_PartRunner;

public class C_AutoMachine implements Runnable {
	// 自动机自己带一个队列，也可以不这样设计，新建另一个类用来管理监控队列
	// 一般最好设置对列大小，以防吃爆内存
	private static final LinkedBlockingQueue<OneLineInTask> C_Queue = new LinkedBlockingQueue<>(99999);

	//这个是本地线程私有的，用volatile修饰保证每次获取的正确性，因为为单独自己使用，所以不用考虑其他并发性问题
	private volatile C_PartOfTask c_PartOfTask;
	
	public C_AutoMachine() {

	}

	@Override
	public void run() {
		machineRun();
	}

	private void machineRun() {
		OneLineInTask oneLineInTask = null;
		boolean isContain = true;
		while (true) {
			oneLineInTask = getTask();
			isContain = isContainPartC(oneLineInTask);
			if (true == isContain) {
				ifContainThenDo(oneLineInTask);
			} else {
				D_AutoMachine.putOneLine(oneLineInTask);
			}
		}
	}

	private OneLineInTask getTask() {
		OneLineInTask oneLineInTask = null;
		try {
			oneLineInTask = C_Queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return oneLineInTask;
	}
	//这个方法也可以直接返回一个D_PartOfTask对象，对象值可以为null
	private boolean isContainPartC(final OneLineInTask oneLineInTask) {
		CheckNull.checkNull(oneLineInTask);
		final Object object = GetPartFromLine.getPartC(oneLineInTask);
		c_PartOfTask = (C_PartOfTask) object;
		return CheckPart.isPartC(object);
	}

	private void ifContainThenDo(final OneLineInTask oneLineInTask) {
		CheckNull.checkNull(c_PartOfTask);
		final int threadNum = prepareForBuildRunner();
		buildRunner(threadNum, oneLineInTask);
	}

	private int prepareForBuildRunner() {
		int threadNum = -1;
		threadNum = c_PartOfTask.getHowManyThreadsWorkOnThisPart();
		return threadNum;
	}

	private void buildRunner(final int threadNum, final OneLineInTask oneLineInTask) {
		C_PartRunner c_PartRunner = null;
		if (threadNum > 0) {
			for (int i = 0; i < threadNum; i++) {
				c_PartRunner = TaskFactoryC.createRunner(oneLineInTask);
				WorkThreadPool.CPart_ThreadPool.submit(c_PartRunner);
			}
		} else {
			WriteLog.putErrorLog("thread num must not be zero. in C_automachine .");
			return;
		}
	}

	public static void putOneLine(final OneLineInTask oneLineInTask) {
		try {
			C_Queue.put(oneLineInTask);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
