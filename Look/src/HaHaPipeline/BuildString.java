package HaHaPipeline;

import HaHaPipeline.task.OneLineInTask;

public class BuildString {

	private BuildString() {

	}

	// 这个方法可能是多余的，直接用加号也应该是没问题的
	public static String buildLineName(final String taskName, final int lineNum) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(taskName);
		stringBuilder.append(lineNum);
		return stringBuilder.toString();
	}
	
	public static String buildGlobalName(final OneLineInTask oneLineInTask,final String partName) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(oneLineInTask.getLineName());
		stringBuilder.append('_');
		stringBuilder.append(partName);
		return stringBuilder.toString();
	}
	
}
