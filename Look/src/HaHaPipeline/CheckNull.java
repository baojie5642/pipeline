package HaHaPipeline;

public class CheckNull {

	private CheckNull() {

	}

	public static void checkNull(final Object object) {
		if (null == object) {
			System.out.println("object is null .");
			WriteLog.putErrorLog("object is null in CheckNull.class.");
			throw new NullPointerException("object is null.");
		}
	}

}
