package TestDeadLock;

import java.util.concurrent.ThreadLocalRandom;

import TestDeadLock.HaHaLock.InnerLock;

public class A_Thread implements Runnable {

	private final Object objectLock0;
	private final byte[] byteLock1;
	private final InnerLock innerLock2;

	private A_Thread(final Object objectLock, final byte[] byteLock, final InnerLock innerLock) {
		this.objectLock0 = objectLock;
		this.byteLock1 = byteLock;
		this.innerLock2 = innerLock;
	}

	public static A_Thread build(final Object objectLock, final byte[] byteLock, final InnerLock innerLock) {
		A_Thread a_Thread = new A_Thread(objectLock, byteLock, innerLock);
		return a_Thread;
	}

	@Override
	public void run() {
		int breakFlag = 0;
		int lockOrder = -7;
		while (true) {
			lockOrder = ThreadLocalRandom.current().nextInt(6);
			System.out.println("lockOrder in athread:"+lockOrder);
			if (lockOrder == 0) {
				lockOrder0(breakFlag);
			} else if (lockOrder == 1) {
				lockOrder1(breakFlag);
			} else if (lockOrder == 2) {
				lockOrder2(breakFlag);
			} else if (lockOrder == 3) {
				lockOrder3(breakFlag);
			} else if (lockOrder == 4) {
				lockOrder4(breakFlag);
			} else if (lockOrder == 5) {
				lockOrder5(breakFlag);
			}
			if(breakFlag==1000){
				break;
			}
		}
	}

	private void lockOrder0(int breakFlag) {
		synchronized (objectLock0) {
			synchronized (byteLock1) {
				synchronized (innerLock2) {
					System.out.println(breakFlag);
					breakFlag++;
				}
			}
		}
	}

	private void lockOrder1(int breakFlag) {
		synchronized (objectLock0) {
			synchronized (innerLock2) {
				synchronized (byteLock1) {
					System.out.println(breakFlag);
					breakFlag++;
				}
			}
		}
	}

	private void lockOrder2(int breakFlag) {
		synchronized (byteLock1) {
			synchronized (objectLock0) {
				synchronized (innerLock2) {
					System.out.println(breakFlag);
					breakFlag++;
				}
			}
		}
	}

	private void lockOrder3(int breakFlag) {
		synchronized (byteLock1) {
			synchronized (innerLock2) {
				synchronized (objectLock0) {
					System.out.println(breakFlag);
					breakFlag++;
				}
			}
		}
	}

	private void lockOrder4(int breakFlag) {
		synchronized (innerLock2) {
			synchronized (objectLock0) {
				synchronized (byteLock1) {
					System.out.println(breakFlag);
					breakFlag++;
				}
			}
		}
	}

	private void lockOrder5(int breakFlag) {
		synchronized (innerLock2) {
			synchronized (byteLock1) {
				synchronized (objectLock0) {
					System.out.println(breakFlag);
					breakFlag++;
				}
			}
		}
	}

}
