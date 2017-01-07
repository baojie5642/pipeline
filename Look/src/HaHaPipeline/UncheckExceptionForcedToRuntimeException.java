package HaHaPipeline;

public class UncheckExceptionForcedToRuntimeException {
	
	public static void forcedTo(final Throwable t) {
		CheckNull.checkNull(t);
		realWork(t);
	}

	private static RuntimeException realWork(final Throwable t) {
		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else if (t instanceof Error) {
			throw (Error) t;
		} else {
			throw new IllegalStateException("Not unchecked : " + t);
		}
	}

}
