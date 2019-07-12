package gmailScraper;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MainFile {
	
	public static void main(String[] args) {
	
		SpringApplicationBuilder builder = new SpringApplicationBuilder(MainFile.class);
	    builder.headless(false);
	    builder.run(args);
		
	}

}
 