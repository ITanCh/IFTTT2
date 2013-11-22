/*
 * where the software begin
 * 
 */
package ifttt2;

import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author itanch
 */
public class mainClass implements Runnable{
    public static ArrayList<myTask> taskList=new ArrayList();      //havs all tasks
    public static ArrayList<myTask> runList=new ArrayList();        //running tasks in it
    public static MainFrame f=new MainFrame();
    static volatile int runCount=0;                                 //runlist size cannot be optimized
    public mainClass() {}
    
    //put tasks in this list
    public static boolean addTask(myTask t){
        return taskList.add(t);
    }
    //get the task with this name
    public static myTask getTask(String n){
         for(int i=0;i<taskList.size();i++){
            if(taskList.get(i).getName().equals(n)){
                return taskList.get(i);
            }
         }
         return null;
    }
    
    //remove a task with this name
    public static boolean removeTask(String n){
        if(n==null)return false;
        for(int i=0;i<taskList.size();i++){
            if(taskList.get(i).getName().equals(n)){
                if(taskList.get(i).isRunning())return false;    //a running task cannot be deleted
                taskList.remove(i);
                return true;
            }
        }
        return false;
    }
    //run a task
    public static boolean addRunTask(String n){
        if(n==null)return false;
        for(int i=0;i<taskList.size();i++){
            if(taskList.get(i).getName().equals(n)){
                myTask task=taskList.get(i);
                if(runList.contains(task))return false;
                task.changeRunFlag(true);
                runList.add(task);
                task.changeRunningInfo("\nRunning...\nBut I do not meet the conditions...");
                return true;
            }
        }
        return false;
    }
    
    //stop a task
    public static boolean removeRunTask(String n){
        if(n==null)return false;
        for(int i=0;i<runList.size();i++){
            if(runList.get(i).getName().equals(n)){
                myTask task=runList.get(i);
                task.changeRunFlag(false);
                task.changeRunningInfo("\nHelp! I haven been stopped...\nYou can run me again...");    
                runList.remove(task);
                return true;
            }
        }
        return false;
    } 
    //a thread in which all tasks run in it
    public static void runTask(){
        while(true){
            runCount=runList.size();
            for(int i=0;i<runCount;i++){              //runList.size() will be optimized as 0 !!! 
                new Activator(runList.get(i)).startTask();
            }
        }
    }
    
    @Override
    public void run() {
        try{
        while(true){
            runCount=runList.size();
            int count=-1;           //runList.size() will be optimized as 0 !!! 
            for(int i=0;i<runCount;i++){ 
                new Activator(runList.get(i)).startTask();
                runCount=runList.size(); 
            }
            for(int i=0;i<mainClass.taskList.size();i++){
                    if(taskList.get(i).getName().equals(f.taskName())){
                        myTask task=mainClass.taskList.get(i);
                        f.printText(task);
                        break;
                    }
             }
            Thread.sleep(10);
        }
        }catch(InterruptedException ex){
            System.out.println("Thread Stop!!!!");
            ex.printStackTrace();
        }
    }
     
    public static void main(String[] args){
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        mainClass mc=new mainClass();
        Thread t=new Thread(mc);
        t.start();
    }

   
    
}
