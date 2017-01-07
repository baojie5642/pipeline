package HaHaPipeline.task.parttask;

import java.util.concurrent.atomic.AtomicInteger;

import HaHaPipeline.MakeRandomNum;

public class A_PartOfTask {

	// 是每个taskline中的一部分，所有的部分组成整个taskline
	// 每一个taskline不一定要有全部的abcd部分，由用户设置
	/*
	 * taskline----|part-A|---|partB|---|partC|---|partD|----taskline
	 */
	private final String partLineTaskName;

	private final int howManyThreadsWorkOnThisPart;

	private final AtomicInteger copyFromThreadNum;

	public static A_PartOfTask build(final String partLineTaskName) {
		return new A_PartOfTask(partLineTaskName);
	}

	private A_PartOfTask(final String partLineTaskName) {
		this.partLineTaskName = partLineTaskName;
		// 这里的每个部分的并发数量还是不少的，因为这是line里面的一部分而已，而且task定义很多且每个task里面定义很多line，那线程会很多的
		this.howManyThreadsWorkOnThisPart = MakeRandomNum.randomNumAddOne(8);
		this.copyFromThreadNum = new AtomicInteger(howManyThreadsWorkOnThisPart);
	}

	// 这里的方法主要是为了，使每部分完成之后，保证会有一个线程将任务提交到下一个流水线区域
	public boolean isThisPartFinished() {
		final int isDone = copyFromThreadNum.decrementAndGet();
		if (0 == isDone) {
			//如果是此部分的最后一个线程调用这个方法，那么要把初始化的线程数重置，以便于下一次定时调度
			resetThreadNum();
			return true;
		} else {
			return false;
		}
	}

	// 此处使用sync，因为竞争不多，每部分的工作线程可能也就几十个，不多，而且这里只是原子性的改变，没有更多的和业务（上层）贴近，所以使用sync
	// 但是后来想了一下，也不需要用同步，哈哈
	private void resetThreadNum() {
		if (0 == copyFromThreadNum.get()) {
			copyFromThreadNum.set(howManyThreadsWorkOnThisPart);
		} else {
			//这个异常主要是为了防止一下，这里几乎不会出问题
			throw new IllegalStateException("work thread num in part is wrong.check it ……");
		}
	}

	public String getPartLineTaskName() {
		return partLineTaskName;
	}

	public int getHowManyThreadsWorkOnThisPart() {
		return howManyThreadsWorkOnThisPart;
	}

}
