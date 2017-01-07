package AutoMachine;

import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.taskrunner.C_PartRunner;

public class TaskFactoryC {
	
	private TaskFactoryC() {

	}

	public static C_PartRunner createRunner(final OneLineInTask oneLineInTask) {
		return C_PartRunner.create(oneLineInTask);
	}
	
}
