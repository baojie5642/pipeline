package HaHaPipeline;

import io.netty.util.internal.ThreadLocalRandom;

public class MakeRandomNum {

	private MakeRandomNum(){
		
	}
	
	public static int randomNumWithoutAddOne(final int bound){
		return roll(bound);
	}
	
	public static int randomNumAddOne(final int bound){
		return roll(bound)+1;
	}
	
	private static int roll(final int bound){
		return ThreadLocalRandom.current().nextInt(bound);
	}
	
}
