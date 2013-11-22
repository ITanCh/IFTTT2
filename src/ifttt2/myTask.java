/*
 * this is a task class which can record some information and has some 
 * methods of this task
 * 
 */
package ifttt2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import weibo4j.Timeline;
import weibo4j.model.WeiboException;

/**
 *
 * @author itanch
 */
public class myTask implements Task {
    //THIS:trigger events
    public final static int TIME=0;
    public final static int RECEIVE_GMAIL=1;
    
    //THAT:result
    public final static int MICROBLOG=0;
    public final static int SEND_GMAIL=1;
    String taskName;
    int thisType;        //the kind of THIS:time?gmail?......
    int thatType;        //the kind of THAT:send mail?send microblog?
    
    //THIS is time things
    String timeD;
    String timeT;
    //THIS is get mails
    GetMail gm;
    String getAddress;
    String getPW;
    
    //THAT is microblog
    String miToken;
    String miContents;
    //THAT is send mail
    String maAddress;
    String maContents;
    
    //task running information
    boolean runFlag=false;
    String  runInfo;
    
    public myTask(){}
    public myTask(String name,int thisT,int thatT){
        taskName=name;
        thisType=thisT;
        thatType=thatT;
        runInfo="I haven't run yet...\nDo you want to run me?";
    }
    
    //set THIS time
    public void setTime(String d,String t){
        timeD=d;
        timeT=t;
    }
    
    //set THIS get mail
    public void setGetMail(String a,String p){
        getAddress=a;
        getPW=p;
        try {
            gm=new GetMail(a,p);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(myTask.class.getName()).log(Level.SEVERE, null, ex);
            runInfo+="\nThis mail have some problem ";
        } catch (MessagingException ex) {
            Logger.getLogger(myTask.class.getName()).log(Level.SEVERE, null, ex);
            runInfo+="\nThis mail have some problem ";
        }
    }
   //set THAT microblog
    public void setMicroBlog(String token,String c){
        miToken=token;
        miContents=c;
    }
    //set THAT mail
    public void setSendMail(String a,String c){
        maAddress=a;
        maContents=c;
    }
    
    
    public String getName(){
        return taskName;
    }
    public int getThisType(){
        return thisType;
    }
    public int getThatType(){
        return thatType;
    }
    public String getTimeD(){
        return timeD;
    }
    public String getTimeT(){
        return timeT;
    }
   
    public String getMiToken(){
        return miToken;
    }
    public String getMiContents(){
        return miContents;
    }
    public String getMaAddress(){
        return maAddress;
    }
    public String getMaContents(){
        return maContents;
    }
    public String getGetAddress(){
        return getAddress;
    }
    public String getGetPW(){
        return getPW;
    }
    
    //a method to show some information about this task
    public String showInformation(){
        String d=taskName+":\n\n";
        //THIS: about time
        if(thisType==TIME){
            d=d+"THIS\nTimer:   "+timeD+" "+timeT+"\n";
        }
        //THIS:about receiving gmail
        else if(thisType==RECEIVE_GMAIL){
             d=d+"THIS\nGet Mail:\n In Address:"+getAddress+"\n";
        }
                  
       //THAT:about microblog
       if(thatType==MICROBLOG){
            d=d+"THAT\nSend Weibo\n"+"Authorised: "+(miToken!=null?"yes":"no")+"\n"+"Contents:\n "+miContents;
       }
        //THAT:about sending mail
        else if(thatType==myTask.SEND_GMAIL){
            d=d+"THAT\nSend Mail\n"+"Send To: "+maAddress+"\n"+"Contents:\n "+maContents;
        }  
       return d;
    }
    public String showRunningInfo(){
        return runInfo;
    }
    public void changeRunningInfo(String s){
        runInfo+=s;
    }
    public void changeRunFlag(boolean b){
        runFlag=b;
    }
    public boolean isRunning(){
        return runFlag;
    }
    @Override
    public boolean THIS() {
        runInfo="\nRunning...\nBut I do not meet the conditions...";
        switch(thisType){
            case TIME:
                    if(timeOut())return true;
                    break;
            case RECEIVE_GMAIL:
                            try {
                                if(gm.getNewMail()){
                                    runInfo+="\nI get a Mail!!";
                                    return true;
                                }
                            } catch (MessagingException ex) {
                                runInfo+="\nI cannot get Mails!";
                                Logger.getLogger(myTask.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
        }
        return false;
    }
    //if time is up
    public boolean timeOut() {
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date timer=sd.parse(timeD+" "+timeT);
            Date now=new Date();
            long diff=(timer.getTime()-now.getTime())/100;     //1/10seconds
            runInfo+="\n"+diff/10+" s left...";
            if(diff<5&&diff>-5)return true;                    //time out,+/-0.5s
        }catch (ParseException e) { 
		runInfo+="\nFormat of the time is wrong...\nCorrect time please...";
                return false;
	}
        return false;
    }

    @Override
    public void THAT() {
        switch(thatType){
            case MICROBLOG: 
                            try {
                                sendMicroblog();
                                runInfo+="\nI have sent this Weibo...\n ";
                            } catch (WeiboException ex) {
                                Logger.getLogger(myTask.class.getName()).log(Level.SEVERE, null, ex);
                                runInfo+="Sorry,send the weibo unsuccessfully...";
                            }
                            break;
                
            case SEND_GMAIL:SendMail sm=new SendMail(maAddress,maContents);
                             try {
                                   sm.send();
                                   runInfo+="\nI have sent this e-mail...\n ";
                                } catch (Exception ex) {
                                    Logger.getLogger(myTask.class.getName()).log(Level.SEVERE, null, ex);
                                    runInfo+="\nSorry,send the mail unsuccessfully...";
                                }
                            break;
        } 
        
        if(thisType==TIME){             //timer only do once
            mainClass.removeRunTask(this.taskName); 
        }
    }
    //this method can send microbolg
    public void sendMicroblog() throws WeiboException{
        String access_token=miToken;  //it is test uid 
        String statuses=miContents;  
        //Weibo weibo = new Weibo();  
        Timeline tm = new Timeline();  
        tm.client.setToken(access_token); 
        tm.UpdateStatus(statuses);  
       
    }  
    
}
