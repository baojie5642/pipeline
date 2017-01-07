package nettytest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MyCache {

	protected static final ConcurrentHashMap<Long, MyChannel> MyCache=new ConcurrentHashMap<Long, MyChannel>();
	
	protected static final ThreadPoolExecutor CacheThreadPoolForMonitor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
	
	protected static final ThreadPoolExecutor CacheThreadPoolForWork=(ThreadPoolExecutor)Executors.newCachedThreadPool();
	
}
