create database ScrapedEmails;
use ScrapedEmails;

CREATE TABLE emailDump (   
id              INT unsigned NOT NULL AUTO_INCREMENT,   
sender          VARCHAR(150) NOT NULL,    
subject         VARCHAR(150) NOT NULL,    
email		VARCHAR(150) NOT NULL,
PRIMARY KEY     (id) 	);