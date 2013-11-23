/*
 * send a mail to a given mail
 * use your e-mail
 */
package ifttt2;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author lTaChi
 */
public class SendMail
{
    
    private String host = "smtp.???.com";               // smtp
    private String username = "??????";            //it is my test mail,you can change it if necessary
    private String password = "?????";             //hehe......please donot use  my mail to do something illegal
    private String mail_head_name = "??????";
    private String mail_head_value = "????????";
    private String mail_to;
    private String mail_from="?????????";
    private String mail_subject = "lTaChiIFTTT:You have a new message";
    private String mail_body;
    private String personalName = "lTaChiIFTTT";
    
    public SendMail(){};
    public SendMail(String mt,String b)
    {
        mail_to=mt;
        mail_body=b;
    }
    
    public void setMailTo(String t){
        mail_to=t;
    }
    public void setMailBody(String b){
        mail_body=b;
    }
    
    //send mail
    public void send() throws Exception
    {
        try
        {
            Properties props = new Properties(); //set properties
            Authenticator auth = new Email_Autherticator(); // authentica this test mail
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props, auth);// set session
            MimeMessage message = new MimeMessage(session);             //ceate a mail
            message.setSubject(mail_subject);
            message.setText(mail_body);
            message.setHeader(mail_head_name, mail_head_value);
            message.setSentDate(new Date());
            Address address = new InternetAddress(mail_from, personalName);
            message.setFrom(address);
            Address toAddress = new InternetAddress(mail_to);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            Transport.send(message); 
            System.out.println("send ok!");
        } catch (MessagingException | UnsupportedEncodingException ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    //authertica test mail
    public class Email_Autherticator extends Authenticator
    {
        public Email_Autherticator()
        {
            super();
        }
        
        @Override
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }
    }

    
}