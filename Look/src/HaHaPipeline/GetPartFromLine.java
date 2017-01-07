package HaHaPipeline;

import HaHaPipeline.task.OneLineInTask;
import HaHaPipeline.task.parttask.A_PartOfTask;
import HaHaPipeline.task.parttask.B_PartOfTask;
import HaHaPipeline.task.parttask.C_PartOfTask;
import HaHaPipeline.task.parttask.D_PartOfTask;

public class GetPartFromLine {

	private static final String APart = "A";
	private static final String BPart = "B";
	private static final String CPart = "C";
	private static final String DPart = "D";

	private GetPartFromLine() {

	}

	// 这里要不要使用这种小火车的形式获取对象也是值得考虑的，可能是final的可以这么做？还是说因为这里的方法不经常调用所以可以这么写？
	// 如果新声明一个对象map，然后map=get("A")的这种形式，也是可以的，这时不会创建对象，只是获取了一下引用不会有new的过程
	// 依照个人喜好吧
	public static A_PartOfTask getPartA(final OneLineInTask oneLineInTask) {
		A_PartOfTask a_PartOfTask = null;
		try {
			a_PartOfTask = (A_PartOfTask) oneLineInTask.getPartsMap().get(APart);
		} catch (Throwable e) {// 这里仅仅是为了防止所有异常，因为有取出为null的情况，所以此处使用了throwable，一般不建议使用，谨慎处理
			assert true;// ignore
		}
		return a_PartOfTask;
	}

	public static B_PartOfTask getPartB(final OneLineInTask oneLineInTask) {
		B_PartOfTask b_PartOfTask = null;
		try {
			b_PartOfTask = (B_PartOfTask) oneLineInTask.getPartsMap().get(BPart);
		} catch (Throwable e) {
			assert true;
		}
		return b_PartOfTask;
	}

	public static C_PartOfTask getPartC(final OneLineInTask oneLineInTask) {
		C_PartOfTask c_PartOfTask = null;
		try {
			c_PartOfTask = (C_PartOfTask) oneLineInTask.getPartsMap().get(CPart);
		} catch (Throwable e) {
			assert true;
		}
		return c_PartOfTask;
	}

	public static D_PartOfTask getPartD(final OneLineInTask oneLineInTask) {
		D_PartOfTask d_PartOfTask = null;
		try {
			d_PartOfTask = (D_PartOfTask) oneLineInTask.getPartsMap().get(DPart);
		} catch (Throwable e) {
			assert true;
		}
		return d_PartOfTask;
	}

}
