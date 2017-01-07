package AutoMachine;

import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.taskrunner.A_PartRunner;

public class TaskFactoryA {

	private TaskFactoryA(){
		
	}
	
	public static A_PartRunner createRunner(final OneLineInTask oneLineInTask){
		return A_PartRunner.create(oneLineInTask);
	}

}
