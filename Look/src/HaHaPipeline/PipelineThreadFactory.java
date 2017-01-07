package HaHaPipeline;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PipelineThreadFactory implements ThreadFactory {
	
	private final UncaughtExceptionHandler unCaughtException = new UncaughtException();
	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final AtomicLong threadNumber = new AtomicLong(1);
	private final ThreadGroup group;
	private final String namePrefix;
	private final String factoryName;
	

	public static PipelineThreadFactory init(final String name) {
		return new PipelineThreadFactory(name);
	}
	
	private PipelineThreadFactory(final String name) {
		group = getThreadGroup();
		factoryName = name;
		namePrefix = factoryName + "-" + poolNumber.getAndIncrement() + "-thread-";
	}

	private ThreadGroup getThreadGroup() {
		final SecurityManager sm = getSecurityManager();
		final ThreadGroup threadGroup = innerThreadGroup(sm);
		return threadGroup;
	}
	
	private SecurityManager getSecurityManager() {
		SecurityManager sm = null;
		sm = System.getSecurityManager();
		if (null == sm) {
			System.out.println("SecurityManager is null.");
		}
		return sm;
	}
	
	private ThreadGroup innerThreadGroup(final SecurityManager sm ){
		ThreadGroup threadGroup = null;
		if(null!=sm){
			threadGroup=sm.getThreadGroup();
		}else {
			threadGroup=Thread.currentThread().getThreadGroup();
		}
		if (null == threadGroup) {
			throw new NullPointerException("threadgroup must not be null.");
		}
		return threadGroup;
	}

	@Override
	public Thread newThread(final Runnable r) {
		final Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		setThreadProperties(thread);
		return thread;
	}
	
	private void setThreadProperties(final Thread thread){
		if (thread.isDaemon()){
			thread.setDaemon(false);
		}
		if (thread.getPriority() != Thread.NORM_PRIORITY){
			thread.setPriority(Thread.NORM_PRIORITY);
		}
		thread.setUncaughtExceptionHandler(unCaughtException);
	}
	
}