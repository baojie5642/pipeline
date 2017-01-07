package HaHaPipeline;

import HaHaPipeline.task.parttask.A_PartOfTask;
import HaHaPipeline.task.parttask.B_PartOfTask;
import HaHaPipeline.task.parttask.C_PartOfTask;
import HaHaPipeline.task.parttask.D_PartOfTask;

public class CheckPart {

	private CheckPart(){
		
	}
	
	public static boolean isPartA(final Object object){
		if (null == object) {
			return false;
		} else {
			if (object instanceof A_PartOfTask) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static boolean isPartB(final Object object){
		if (null == object) {
			return false;
		} else {
			if (object instanceof B_PartOfTask) {
				return true;
			} else {
				return false;
			}
		}
	}
	public static boolean isPartC(final Object object){
		if (null == object) {
			return false;
		} else {
			if (object instanceof C_PartOfTask) {
				return true;
			} else {
				return false;
			}
		}
	}
	public static boolean isPartD(final Object object){
		if (null == object) {
			return false;
		} else {
			if (object instanceof D_PartOfTask) {
				return true;
			} else {
				return false;
			}
		}
	}	
	
}
