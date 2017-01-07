package HaHaPipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import AutoMachine.A_AutoMachine;
import AutoMachine.B_AutoMachine;
import AutoMachine.C_AutoMachine;
import AutoMachine.D_AutoMachine;
import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.task.Task;
import HaHaPipeline.task.parttask.A_PartOfTask;
import HaHaPipeline.task.parttask.B_PartOfTask;
import HaHaPipeline.task.parttask.C_PartOfTask;
import HaHaPipeline.task.parttask.D_PartOfTask;
import HaHaPipeline.taskrunner.ScheduledRunner;

public class ScheduledMain {

	public ScheduledMain(){
		
	}
	//入口函数写的很粗糙，哈哈，么么哒
	//自己使用的话，直接把下面的文件路径写成自己正确的就可以直接跑啦
	public static void main(String args[]){
		//首先启动自动机
		A_AutoMachine a_AutoMachine=new A_AutoMachine();
		B_AutoMachine b_AutoMachine=new B_AutoMachine();
		C_AutoMachine c_AutoMachine=new C_AutoMachine();
		D_AutoMachine d_AutoMachine=new D_AutoMachine();
		
		WorkThreadPool.AutoMachine_ThreadPool.submit(a_AutoMachine);
		WorkThreadPool.AutoMachine_ThreadPool.submit(b_AutoMachine);
		WorkThreadPool.AutoMachine_ThreadPool.submit(c_AutoMachine);
		WorkThreadPool.AutoMachine_ThreadPool.submit(d_AutoMachine);
		//按照自己的路径来
		final String errorLogFile="C:\\liuxin\\errorLogFile.txt";
		
		File file=new File(errorLogFile);
		
		if(file.exists()){
			file.setWritable(true);
			file.delete();
			LockSupport.parkNanos(TimeUnit.NANOSECONDS.convert(3, TimeUnit.MILLISECONDS));
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			file.setWritable(true);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		WriteLog writeLog=WriteLog.create(file);
		
		WorkThreadPool.WriteErrorLog_ThreadPool.submit(writeLog);
		
		
		final String[] partArray={"A","B","C","D"};
		
		final String taskName="Task";
		
		final String lineName="Line";
		//task中line的数量
		final int linesBound=20;//标号从0到29
		//line中part的数量
		final int partBound=4;//一共4个部分，可以自由增加
		//task的数量
		final int taskToPrepare=150;
		
		OneLineInTask oneLineInTask=null;
		
		Task task=null;
		
		A_PartOfTask a_PartOfTask=null;
		
		B_PartOfTask b_PartOfTask=null;
		
		C_PartOfTask c_PartOfTask=null;
		
		D_PartOfTask d_PartOfTask=null;
		
		int howManyLinesByRandom=-1;
		
		int howManyPartByRandom=-1;
		
		Map<String, Object> partMap=null;
		
		ArrayList<OneLineInTask> lines=null;
		
		Semaphore taskSemaphore=null;
		
		ScheduledRunner scheduledRunner=null;
		//下面这种随机的初始化方式很容易造成不平衡性问题，从工程的角度开看是不可取的，但是这里这样初始化只是示意一下，可以把每部分都初始化完整
		//因为每个自动机处理的量是不一样的，所以造成了平衡性问题
		for(int i=0;i<taskToPrepare;i++){
			
			howManyLinesByRandom=ThreadLocalRandom.current().nextInt(linesBound);
			
			lines=new ArrayList<>(howManyLinesByRandom+1);
			taskSemaphore =new Semaphore(howManyLinesByRandom+1);
			
			for(int j=0;j<howManyLinesByRandom+1;j++){
				
				howManyPartByRandom=3;//ThreadLocalRandom.current().nextInt(partBound);
				
				if(howManyPartByRandom==0){
					a_PartOfTask=A_PartOfTask.build(taskName+i+lineName+j+"_"+howManyPartByRandom);
					partMap=new HashMap<>(howManyPartByRandom+1);
					partMap.put(partArray[0], a_PartOfTask);
				}else{
					partMap=new HashMap<>(howManyPartByRandom+1);
					for(int k=0;k<howManyPartByRandom+1;k++){
						if(k==0){
							a_PartOfTask=A_PartOfTask.build(taskName+i+lineName+j+"_"+k);
							partMap.put(partArray[k], a_PartOfTask);
						}else if (k==1) {
							b_PartOfTask=B_PartOfTask.build(taskName+i+lineName+j+"_"+k);
							partMap.put(partArray[k], b_PartOfTask);
						}else if(k==2){
							c_PartOfTask=C_PartOfTask.build(taskName+i+lineName+j+"_"+k);
							partMap.put(partArray[k], c_PartOfTask);
						}else if (k==3) {
							d_PartOfTask=D_PartOfTask.build(taskName+i+lineName+j+"_"+k);
							partMap.put(partArray[k], d_PartOfTask);
						}
					}
				}
				oneLineInTask=OneLineInTask.create(taskName, howManyPartByRandom+1, j, partMap, taskSemaphore);
				lines.add(j, oneLineInTask);
			}
			
			task=Task.create(taskName, i, howManyLinesByRandom+1, lines, taskSemaphore);
			
			scheduledRunner=ScheduledRunner.create(task);
			
			WorkThreadPool.Scheduled_ThreadPool.scheduleWithFixedDelay(scheduledRunner, task.getWaitSecondOnlyBeforeFirstStart(), task.getDelaySecondAfterFinish(), TimeUnit.SECONDS);
			
		}
			
	}
	
}
