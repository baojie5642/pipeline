package nettytest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class MyChannel {

	private final AtomicBoolean isTimeOut = new AtomicBoolean(false);
	private final ChannelHandlerContext ctxInMap;
	private final ReentrantLock mainLock=new ReentrantLock();
	private final Condition condition;
	private final long messageID;
	private final LittleMessage littleMessage;

	private MyChannel(final ChannelHandlerContext ctxInMap,final long messageID) {
		this.ctxInMap = ctxInMap;
		this.messageID = messageID;
		this.littleMessage = new LittleMessage(messageID);
		this.condition = mainLock.newCondition();
	}

	public static MyChannel create(final ChannelHandlerContext ctxInMap, long messageID) {
		MyChannel myChannel = new MyChannel(ctxInMap, messageID);
		return myChannel;
	}

	public void startMonitor() {
		final ChannelMonitor channelMonitor = new ChannelMonitor();
		MyCache.CacheThreadPoolForMonitor.submit(channelMonitor);
	}

	public void weakUpIfWait(final String reponse) {
		final ReentrantLock lock = mainLock;
		boolean innerTimeOut = false;
		lock.lock();
		try {
			innerTimeOut = isTimeOut.get();
			if (true == innerTimeOut) {
				return;
			} else {
				// condition.notify();
				littleMessage.setReponse(reponse);
				final byte[] bytes = ObjectToByte.toByte(littleMessage);
				ChannelFuture channelFuture=ctxInMap.writeAndFlush(bytes);
				channelFuture.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) {
						System.out.println("……自己添加……");
					}
				});
				MyCache.MyCache.remove(messageID);
				isTimeOut.set(true);
				// 这里要注意lock中 condition的用法和被唤醒特性，放到最开始也是可以的,放到下面可能逻辑更合理些。
				condition.notify();
			}
		} finally {
			lock.unlock();
		}
	}

	private final class ChannelMonitor implements Runnable {

		public ChannelMonitor() {

		}

		@Override
		public void run() {
			boolean innerFlag = false;
			final ReentrantLock lock = mainLock;
			lock.lock();
			try {
				innerFlag = isTimeOut.get();
				if (true == innerFlag) {
					MyCache.MyCache.remove(messageID);
					return;
				} else {
					waitTenMills(10, TimeUnit.MILLISECONDS);
					innerFlag = isTimeOut.get();
					if (true == innerFlag) {
						return;
					} else {
						littleMessage.setReponse("timeout");
						final byte[] bytes = ObjectToByte.toByte(littleMessage);
						ChannelFuture channelFuture=ctxInMap.writeAndFlush(bytes);
						channelFuture.addListener(new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture future) {
								System.out.println("……自己添加……");
							}
						});
						MyCache.MyCache.remove(messageID);
						isTimeOut.set(true);
					}
				}
			} finally {
				lock.unlock();
			}
		}

		private void waitTenMills(final int waitTime, final TimeUnit unit) {
			try {
				condition.await(waitTime, unit);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
