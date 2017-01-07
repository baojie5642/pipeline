package AutoMachine;

import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.taskrunner.B_PartRunner;

public class TaskFactoryB {
	
	private TaskFactoryB() {

	}

	public static B_PartRunner createRunner(final OneLineInTask oneLineInTask) {
		return B_PartRunner.create(oneLineInTask);
	}
	
}
