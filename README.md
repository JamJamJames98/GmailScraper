# GmailScraper

README:

Database Setup:

Install mysql server
Run mysql server on port 3306 (DEFAULT)
Connect to server(Through command line is fine)
Run the SQL in "setup.sql.txt"

Java Files
AuthGoogle.java - Used to authenticate the user via Google's OAUTH.

FetchEmail.java - Runnable class used to multi thread the email scrape since individual email data can only scraped one at a time.

MainFile.java - The main file that is run to start the application

mySQL.java - Used to complete database operations (insert, view, delete)

WebpageController.java - Holds the spring boot webpage functions

How to run:
Simply load the project in an IDE (eclipse preferably) and run 'MainFile.java'.

Then go to http://localhost:8080/index.

Hit the 'Login' button and authenticate with google

Hit the 'Scrape Emails' button and check the IDE console to see scrape progress

Hit the 'View Emails' button to view emails associated with your google authenticated account. (Only you can access your emails)
