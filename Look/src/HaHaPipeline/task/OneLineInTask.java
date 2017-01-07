package HaHaPipeline.task;

import java.util.Map;
import java.util.concurrent.Semaphore;

import HaHaPipeline.BuildString;

public class OneLineInTask {

	// task内部的一条线
	// 顺序是这样的：A is 0,B is 1,C is 2,D is 3 ,但是不管是怎么样，一定要顺序出现，从0到3这种顺序
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

	// 这里的object对应line里面的每个部分，因为这里之定义了4个部分，所以最大值设置为4

	private final Map<String, Object> partsMap;

	private final Semaphore taskSemaphore;

	private final int howManyPartInLine;

	private final String lineName;

	private final String taskName;

	private final int lineNum;

	private OneLineInTask(final String taskName, final int howManyPartInLine, final int lineNum,
			final Map<String, Object> partsMap, final Semaphore taskSemaphore) {
		this.taskName = taskName;
		this.howManyPartInLine = howManyPartInLine;
		this.lineNum = lineNum;
		this.partsMap = partsMap;
		this.taskSemaphore = taskSemaphore;
		this.lineName = BuildString.buildLineName(taskName,lineNum);//taskName + lineNum;
	}
	
	public static OneLineInTask create(final String taskName, final int howManyPartInLine, final int lineNum,
			final Map<String, Object> partsMap, final Semaphore taskSemaphore) {
		return new OneLineInTask(taskName, howManyPartInLine, lineNum, partsMap, taskSemaphore);
	}

	public void releaseOneSemaphore(){
		taskSemaphore.release(1);
	}
	
	public int getHowManyPartInLine() {
		return howManyPartInLine;
	}

	public int getLineNum() {
		return lineNum;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getLineName() {
		return lineName;
	}

	public Map<String, Object> getPartsMap() {
		return partsMap;
	}

	public Semaphore getTaskSemaphore() {
		return taskSemaphore;
	}
	
}
