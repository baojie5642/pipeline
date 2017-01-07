package test;

public class SyncObject {

	public SyncObject(){
		
	}
	
	private static void print(){
		System.out.println("static function.");
	}
	
	public  void test1(){
		print();
	}
	
	
	public final void test(){
		print();
	}
	
}
