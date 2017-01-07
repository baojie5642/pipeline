package HaHaPipeline;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkThreadPool {

	// 直接使用了，不过可以继承线程池实现自己的一些定制化

	//这里使用的弹性较大的传递器，可以改成队列也可以，看情况
	
	//但是后来还是改成缓冲的队列了
	public static final ThreadPoolExecutor APart_ThreadPool = new ThreadPoolExecutor(1024, 4096, 160, TimeUnit.SECONDS,
			new SynchronousQueue<>(), PipelineThreadFactory.init("APart_ThreadPool"),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public static final ThreadPoolExecutor BPart_ThreadPool = new ThreadPoolExecutor(1024, 4096, 160, TimeUnit.SECONDS,
			new SynchronousQueue<>(), PipelineThreadFactory.init("BPart_ThreadPool"),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public static final ThreadPoolExecutor CPart_ThreadPool = new ThreadPoolExecutor(1024, 4096, 160, TimeUnit.SECONDS,
			new SynchronousQueue<>(), PipelineThreadFactory.init("CPart_ThreadPool"),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public static final ThreadPoolExecutor DPart_ThreadPool = new ThreadPoolExecutor(1024, 4096, 160, TimeUnit.SECONDS,
			new SynchronousQueue<>(), PipelineThreadFactory.init("DPart_ThreadPool"),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public static final ThreadPoolExecutor AutoMachine_ThreadPool = new ThreadPoolExecutor(64, 1024, 120,
			TimeUnit.SECONDS, new LinkedBlockingQueue<>(999), PipelineThreadFactory.init("AutoMachine_ThreadPool"),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public static final ScheduledThreadPoolExecutor Scheduled_ThreadPool = new ScheduledThreadPoolExecutor(512,
			PipelineThreadFactory.init("ScheduledThreadPool"), new ThreadPoolExecutor.CallerRunsPolicy());

	public static final ThreadPoolExecutor WriteErrorLog_ThreadPool = new ThreadPoolExecutor(64, 1024, 120,
			TimeUnit.SECONDS, new LinkedBlockingQueue<>(999), PipelineThreadFactory.init("WriteErrorLog_ThreadPool"),
			new ThreadPoolExecutor.CallerRunsPolicy());

}
