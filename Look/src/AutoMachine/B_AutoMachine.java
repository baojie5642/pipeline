package AutoMachine;

import java.util.concurrent.LinkedBlockingQueue;

import HaHaPipeline.CheckNull;
import HaHaPipeline.CheckPart;
import HaHaPipeline.GetPartFromLine;
import HaHaPipeline.WorkThreadPool;
import HaHaPipeline.WriteLog;
import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.task.parttask.B_PartOfTask;
import HaHaPipeline.taskrunner.B_PartRunner;

public class B_AutoMachine implements Runnable {
	// 自动机自己带一个队列，也可以不这样设计，新建另一个类用来管理监控队列
	// 一般最好设置对列大小，以防吃爆内存
	private static final LinkedBlockingQueue<OneLineInTask> B_Queue = new LinkedBlockingQueue<>(99999);
	//这个是本地线程私有的，用volatile修饰保证每次获取的正确性，因为为单独自己使用，所以不用考虑其他并发性问题
	private volatile B_PartOfTask b_PartOfTask;
	
	public B_AutoMachine() {

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
			isContain = isContainPartB(oneLineInTask);
			if (true == isContain) {
				ifContainThenDo(oneLineInTask);
			} else {
				C_AutoMachine.putOneLine(oneLineInTask);
			}
		}
	}

	private OneLineInTask getTask() {
		OneLineInTask oneLineInTask = null;
		try {
			oneLineInTask = B_Queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return oneLineInTask;
	}
	//这个方法也可以直接返回一个D_PartOfTask对象，对象值可以为null
	private boolean isContainPartB(final OneLineInTask oneLineInTask) {
		CheckNull.checkNull(oneLineInTask);
		final Object object = GetPartFromLine.getPartB(oneLineInTask);
		b_PartOfTask = (B_PartOfTask) object;
		return CheckPart.isPartB(object);
	}

	private void ifContainThenDo(final OneLineInTask oneLineInTask) {
		CheckNull.checkNull(b_PartOfTask);
		final int threadNum = prepareForBuildRunner();
		buildRunner(threadNum, oneLineInTask);
	}

	private int prepareForBuildRunner() {
		int threadNum = -1;
		threadNum = b_PartOfTask.getHowManyThreadsWorkOnThisPart();
		return threadNum;
	}

	private void buildRunner(final int threadNum, final OneLineInTask oneLineInTask) {
		B_PartRunner b_PartRunner = null;
		if (threadNum > 0) {
			for (int i = 0; i < threadNum; i++) {
				b_PartRunner = TaskFactoryB.createRunner(oneLineInTask);
				WorkThreadPool.BPart_ThreadPool.submit(b_PartRunner);
			}
		} else {
			WriteLog.putErrorLog("thread num must not be zero. in B_automachine .");
			return;
		}
	}

	public static void putOneLine(final OneLineInTask oneLineInTask) {
		try {
			B_Queue.put(oneLineInTask);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
