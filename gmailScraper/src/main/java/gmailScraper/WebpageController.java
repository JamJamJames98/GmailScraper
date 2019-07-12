package gmailScraper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;

@Controller
public class WebpageController {
	
	private Gmail service = null;
	
	/*
	 * Check if a user is currently logged in
	 */
	private boolean checkLoggedIn() {
		String curr_user;
		try {
			if (service == null) {
				return false;
			}
			curr_user = service.users().getProfile("me").execute().getEmailAddress();
			if (curr_user == null) {
				return false;
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/*
	 * Reset the service variable and delete any auth tokens saved locally
	 */
	@RequestMapping("/logout")
	public String logout() {
		System.out.println("User hit logout page");
        for (File file : new java.io.File(AuthGoogle.getTokensFilePath()).listFiles()) {
        	file.delete();
        }
        service = null;
        return "home.html";
	}
	
	/*
	 * Load the home page
	 */
	@RequestMapping("/index")
	public String index_page() {
		System.out.println("User hit index page");
        return "home.html";
	}	
	
	/*
	 * Prompt user to authenticate with google then load the login page
	 */
	@RequestMapping("/login")
	public String login_page() {
		System.out.println("User hit login page");
        if (checkLoggedIn() == false) {
            try {
                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                service = new Gmail.Builder(HTTP_TRANSPORT, AuthGoogle.getJsonFactory(), AuthGoogle.getCredentials(HTTP_TRANSPORT)).setApplicationName(AuthGoogle.getApplicationName()).build();
            } catch (IOException | GeneralSecurityException | URISyntaxException e) {
    			return "Error" + e;
    		}
        }
        return "login.html";
	}
	
	/*
	 * Display the scraped emails for the current user if applicable
	 */
	@RequestMapping(value = "/viewEmails", method = RequestMethod.GET, produces = "text/plain; charset=UTF-8")
	@ResponseBody
	public String viewEmails(HttpServletResponse response) throws IOException {
		System.out.println("User hit view emails page");
		if (checkLoggedIn() == false) {
			return "Displaying emails failed..." + System.lineSeparator() + "Please login to display scraped emails";
		}
		String curr_user = service.users().getProfile("me").execute().getEmailAddress();
		String return_result = "";
		return_result = return_result + "Displaying emails for: " + curr_user + System.lineSeparator();
		return_result = return_result + mySQL.getDatabaseValues(curr_user);
		return return_result;
	}

	/*
	 * Scrape the emails for the current user if applicable
	 */
	@RequestMapping("/emailScraper")
	public String emailScraper() {
		System.out.println("User hit email scraper page");
		if (checkLoggedIn() == false) {
			return "please_login.html";
		}
		
		try {			
			mySQL.deleteFromDatabase(service.users().getProfile("me").execute().getEmailAddress());
	        ListMessagesResponse messages = service.users().messages().list("me").setMaxResults((long)500).execute();
	        for(int i = 0; i < messages.getMessages().size(); i++) {
	        	String current_id = messages.getMessages().get(i).getId();
	            FetchEmail cnt = new FetchEmail(current_id,"me",service);
	            new Thread(cnt).start();
	            try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	        System.out.println("Successful scrape");
		} catch (IOException e) {
	        System.out.println("Encountered an error during scraping");
		}
		return "login.html";
	}
	
}
