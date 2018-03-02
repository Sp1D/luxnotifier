use sampledb;

DROP TABLE if exists notification;
DROP TABLE if exists subscription;
DROP TABLE if exists users;


CREATE TABLE users (
  email VARCHAR(100) PRIMARY KEY,
  password VARCHAR(100) NOT NULL
);
CREATE TABLE subscription (
  user_email VARCHAR(100) NOT NULL,
  service_id VARCHAR(20) NOT NULL,
  service_name VARCHAR(100) NOT NULL,
  language_id VARCHAR(20) NOT NULL,
  language_name VARCHAR(100) NOT NULL,
  search_until_date DATE NOT NULL,
  PRIMARY KEY (user_email , service_id),
  FOREIGN KEY (user_email) REFERENCES users (email)
);
CREATE TABLE notification (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_email VARCHAR(100) NOT NULL,
  doctor VARCHAR(100) NOT NULL,
  clinic VARCHAR(100) NOT NULL,
  service VARCHAR(100) NOT NULL,
  date_time DATETIME,
  FOREIGN KEY (user_email) REFERENCES users (email)
)