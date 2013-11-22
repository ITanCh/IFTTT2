/*
 * it is a activator that can  deal with Task class 
 */
package ifttt2
;

import java.util.Date;

public class Activator {
	static Task task;
	Activator(){
		task=null;
	}
	Activator(Task t){
		task=t;
	}
	public  void startTask(){
		if(task.THIS())
			task.THAT();
		else
			System.out.println(new Date());
	}
}
