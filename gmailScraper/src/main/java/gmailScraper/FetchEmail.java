package gmailScraper;

import java.io.IOException;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

class FetchEmail implements Runnable {
   Thread mythread;
   private String current_id;
   private String user;
   private Gmail service;
   FetchEmail(String current_id, String user, Gmail service) { 
      mythread = new Thread();
      this.current_id = current_id;
      this.user = user;
      this.service = service;
      mythread.start();
   }
   /*
    * Each thread runs this
    */
   public void run() {
	   try {
		   Message message = service.users().messages().get(user, current_id).execute();
		   String message_dump = "" + message;
		   String subject = message_dump.substring(message_dump.lastIndexOf("{\"name\":\"Subject\",")+27);
		   subject = subject.substring(0,subject.indexOf("\"}"));
		   subject = subject.replace("'", "");
		   subject = subject.replace("\"", "");
		   
		   String sender = message_dump.substring(message_dump.lastIndexOf("{\"name\":\"From\",")+24);
		   sender = sender.substring(0,sender.indexOf("\"}"));
		   sender = sender.replace("'", "");
		   sender = sender.replace("\"", "");
		   
		   String email = service.users().getProfile(user).execute().getEmailAddress();
		   mySQL.writeToDatabase(sender, subject, email);
	   } catch(IOException e) {
		   System.out.println(mythread + " errored.");
		   System.out.println(e);
	   }
   }
}