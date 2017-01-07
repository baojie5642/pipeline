package AutoMachine;

import java.util.concurrent.LinkedBlockingQueue;

import HaHaPipeline.CheckNull;
import HaHaPipeline.CheckPart;
import HaHaPipeline.GetPartFromLine;
import HaHaPipeline.WorkThreadPool;
import HaHaPipeline.WriteLog;
import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.task.parttask.A_PartOfTask;
import HaHaPipeline.taskrunner.A_PartRunner;

public class A_AutoMachine implements Runnable {
	
	// 自动机自己带一个队列，也可以不这样设计，新建另一个类用来管理监控队列
	// 一般最好设置对列大小，以防吃爆内存
	private static final LinkedBlockingQueue<OneLineInTask> A_Queue = new LinkedBlockingQueue<>(99999);
	//这个是本地线程私有的，用volatile修饰保证每次获取的正确性，因为为单独自己使用，所以不用考虑其他并发性问题
	private volatile A_PartOfTask a_PartOfTask;
	
	public A_AutoMachine() {

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
			isContain = isContainPartA(oneLineInTask);
			if (true == isContain) {
				ifContainThenDo(oneLineInTask);
			} else {
				B_AutoMachine.putOneLine(oneLineInTask);
			}
		}
	}

	private OneLineInTask getTask() {
		OneLineInTask oneLineInTask = null;
		try {
			oneLineInTask = A_Queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return oneLineInTask;
	}
	//这个方法也可以直接返回一个D_PartOfTask对象，对象值可以为null
	private boolean isContainPartA(final OneLineInTask oneLineInTask) {
		CheckNull.checkNull(oneLineInTask);
		final Object object = GetPartFromLine.getPartA(oneLineInTask);
		a_PartOfTask=(A_PartOfTask)object;
		return CheckPart.isPartA(object);
	}

	private void ifContainThenDo(final OneLineInTask oneLineInTask) {
		CheckNull.checkNull(a_PartOfTask);
		final int threadNum = prepareForBuildRunner(a_PartOfTask);
		buildRunner(threadNum, oneLineInTask);
	}

	private void buildRunner(final int threadNum, final OneLineInTask oneLineInTask) {
		A_PartRunner a_PartRunner = null;
		if (threadNum > 0) {
			for (int i = 0; i < threadNum; i++) {
				a_PartRunner = TaskFactoryA.createRunner(oneLineInTask);
				WorkThreadPool.APart_ThreadPool.submit(a_PartRunner);
			}
		} else {
			WriteLog.putErrorLog("thread num must not be zero. in A_automachine .");
			return;
		}
	}

	private int prepareForBuildRunner(final A_PartOfTask a_PartOfTask) {
		int threadNum = -1;
		threadNum = a_PartOfTask.getHowManyThreadsWorkOnThisPart();
		return threadNum;
	}

	public static void putOneLine(final OneLineInTask oneLineInTask) {
		try {
			A_Queue.put(oneLineInTask);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
