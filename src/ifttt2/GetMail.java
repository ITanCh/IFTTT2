/*
 * use it to know if get some mails
 * 
 */
package ifttt2;

import java.util.Properties;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author ITaChi
 */
public class GetMail {
    String addr;
    String pw;
    Properties props;
    Session session;
    Folder folder;
    Store store;
    int mailCount;
    public GetMail(){};
    public GetMail(String a,String p) throws NoSuchProviderException, MessagingException{
        addr=a;
        pw=p;
        mailCount=-1;
        String tail=cutTail(addr);
        String name=cutHead(addr);
        props = new Properties();  
        props.setProperty("mail.store.protocol", "imap"); //pop3
        props.setProperty("mail.imap.host", "imap."+tail);
        
        session=Session.getInstance(props);
        store = session.getStore("imap"); 
        session.setDebug(false);
        store.connect("imap."+tail,name,pw);
        folder = store.getFolder("INBOX");  //get inbox
    }
    
    //to know if get new mails
    public boolean getNewMail() throws MessagingException{
        folder.open(Folder.READ_ONLY);
        System.out.println("open"+folder.isOpen());
        int l=folder.getMessageCount();           //if receive a new mail the count of mails will change
        System.out.println(l);
        if(mailCount==-1||l<mailCount)
            mailCount=l;
        else if(l>mailCount){
            mailCount=l;
            folder.close(false);
            return true;
        }
        System.out.println("open2"+folder.isOpen());
        folder.close(false);
        System.out.println("close"+folder.isOpen());
        return false;
       
    }
    
    private String cutHead(String a){
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='@'){
                return a.substring(0,i);
            }
        }
        return null;
    }
    private String cutTail(String a){
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='@'){
                return a.substring(i+1);
            }
        }
        return null;
    }
  
}

