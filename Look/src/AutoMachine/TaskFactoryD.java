package AutoMachine;

import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.taskrunner.D_PartRunner;

public class TaskFactoryD {

	private TaskFactoryD() {

	}

	public static D_PartRunner createRunner(final OneLineInTask oneLineInTask) {
		return D_PartRunner.create(oneLineInTask);
	}
	
}
