package HaHaPipeline.taskrunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import AutoMachine.C_AutoMachine;
import HaHaPipeline.BuildString;
import HaHaPipeline.CheckPart;
import HaHaPipeline.GetPartFromLine;
import HaHaPipeline.MakeRandomNum;
import HaHaPipeline.WriteLog;
import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.task.parttask.B_PartOfTask;

public class B_PartRunner implements Runnable {

	private static final String partName = "BPart";

	private volatile B_PartOfTask b_PartOfTask;

	private final OneLineInTask oneLineInTask;

	private final int needSecondsDoThisWork;

	private final String globalPartName;

	public static B_PartRunner create(final OneLineInTask oneLineInTask) {
		return new B_PartRunner(oneLineInTask);
	}

	private B_PartRunner(final OneLineInTask oneLineInTask) {
		this.oneLineInTask = oneLineInTask;
		this.needSecondsDoThisWork = MakeRandomNum.randomNumAddOne(20);
		this.globalPartName = BuildString.buildGlobalName(oneLineInTask, partName);
	}

	@Override
	public void run() {
		// 这里的检测其实是二次检测了，在自动机里面其实已经检测一次了，应该可以把这部分的检测去掉，哈哈
		// 先要检测次line里面是否包含流水线的这个部分，如果包含就执行work，如果不包含就直接返回，并将此line移交到另一个部分
		// 但是这里会有问题，因为本来line就没有这个prat，但是却又被投递到了这个部分，那么还是直接打印错误信息或者抛出异常比较好
		final boolean thisPartInLine = isThisPartInLine();
		if (true == thisPartInLine) {
			work();
		} else {
			System.out.println("********************本来没有这个部分，但是还是被投递了，自动机出错，请检查！*********************");
		}
	}

	private boolean isThisPartInLine() {
		final Object object = GetPartFromLine.getPartB(oneLineInTask);
		b_PartOfTask = (B_PartOfTask) object;
		return CheckPart.isPartB(object);
	}

	private void work() {
		if (null == b_PartOfTask) {
			WriteLog.putErrorLog("volatile B_PartOfTask must not be null.");
		} else {
			statrWork();
			doWork();
			finishWork();
		}
	}

	private void statrWork() {
		System.out.println(globalPartName + ":start.");
	}

	private void doWork() {
		// 使用locksupport这里只是不处理异常,模拟工作，投机取巧了，哈哈
		//LockSupport.parkNanos(TimeUnit.NANOSECONDS.convert(needSecondsDoThisWork, TimeUnit.SECONDS));
		try {
			TimeUnit.SECONDS.sleep(needSecondsDoThisWork);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void finishWork() {
		final boolean isLastThread = isIAmTheLastThreadDoThisPart();
		if (true == isLastThread) {
			C_AutoMachine.putOneLine(oneLineInTask);// 如果本地线程是最后一个执行此部分的线程，那么由他来将line放入下一个部分
			System.out.println(globalPartName + ":all thread finish.");
		} else {
			System.out.println(globalPartName + ":part thread finish.");
		}
	}

	private boolean isIAmTheLastThreadDoThisPart() {
		final B_PartOfTask innerB_PartOfTask = b_PartOfTask;
		final boolean isLastThread = innerB_PartOfTask.isThisPartFinished();
		return isLastThread;
	}

	public static String getPartname() {
		return partName;
	}

	public OneLineInTask getOneLineInTask() {
		return oneLineInTask;
	}

	public int getNeedSecondsDoThisWork() {
		return needSecondsDoThisWork;
	}

	public String getGlobalPartName() {
		return globalPartName;
	}

}
