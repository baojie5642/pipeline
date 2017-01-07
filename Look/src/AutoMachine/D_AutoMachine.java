package AutoMachine;

import java.util.concurrent.LinkedBlockingQueue;

import HaHaPipeline.CheckNull;
import HaHaPipeline.CheckPart;
import HaHaPipeline.GetPartFromLine;
import HaHaPipeline.WorkThreadPool;
import HaHaPipeline.WriteLog;
import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.task.parttask.D_PartOfTask;
import HaHaPipeline.taskrunner.D_PartRunner;

public class D_AutoMachine implements Runnable {
	// 自动机自己带一个队列，也可以不这样设计，新建另一个类用来管理监控队列
	// 一般最好设置对列大小，以防吃爆内存
	private static final LinkedBlockingQueue<OneLineInTask> D_Queue = new LinkedBlockingQueue<>(99999);
	//这个是本地线程私有的，用volatile修饰保证每次获取的正确性，因为为单独自己使用，所以不用考虑其他并发性问题
	private volatile D_PartOfTask d_PartOfTask;
	
	public D_AutoMachine() {

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
			isContain = isContainPartD(oneLineInTask);
			if (true == isContain) {
				ifContainThenDo(oneLineInTask);
			} else {
				// 流水线的最后一步，已经出产品了，如果觉得可以，可以将这部分在提交到A自动机，看看会发生什么，哈哈
			}
		}
	}

	private OneLineInTask getTask() {
		OneLineInTask oneLineInTask = null;
		try {
			oneLineInTask = D_Queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return oneLineInTask;
	}
	//这个方法也可以直接返回一个D_PartOfTask对象，对象值可以为null
	private boolean isContainPartD(final OneLineInTask oneLineInTask) {
		CheckNull.checkNull(oneLineInTask);
		final Object object = GetPartFromLine.getPartD(oneLineInTask);
		d_PartOfTask = (D_PartOfTask) object;
		return CheckPart.isPartD(object);
	}

	private void ifContainThenDo(final OneLineInTask oneLineInTask) {
		CheckNull.checkNull(d_PartOfTask);
		final int threadNum = prepareForBuildRunner();
		buildRunner(threadNum, oneLineInTask);
	}

	private int prepareForBuildRunner() {
		int threadNum = -1;
		threadNum = d_PartOfTask.getHowManyThreadsWorkOnThisPart();
		return threadNum;
	}

	private void buildRunner(final int threadNum, final OneLineInTask oneLineInTask) {
		D_PartRunner d_PartRunner = null;
		if (threadNum > 0) {
			for (int i = 0; i < threadNum; i++) {
				d_PartRunner = TaskFactoryD.createRunner(oneLineInTask);
				WorkThreadPool.DPart_ThreadPool.submit(d_PartRunner);
			}
		} else {
			WriteLog.putErrorLog("thread num must not be zero. in D_automachine .");
			return;
		}
	}

	public static void putOneLine(final OneLineInTask oneLineInTask) {
		try {
			D_Queue.put(oneLineInTask);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
