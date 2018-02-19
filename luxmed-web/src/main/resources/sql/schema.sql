DROP TABLE subscription
IF EXISTS;
DROP TABLE user
IF EXISTS;

CREATE TABLE user
(
  email    VARCHAR(100) PRIMARY KEY,
  password VARCHAR(100) NOT NULL
);
CREATE TABLE subscription
(
  user_email        VARCHAR(100) NOT NULL,
  service_id        VARCHAR(20)  NOT NULL,
  service_name      VARCHAR(100) NOT NULL,
  language_id       VARCHAR(20)  NOT NULL,
  language_name     VARCHAR(100) NOT NULL,
  search_until_date DATE         NOT NULL,
  PRIMARY KEY (user_email, service_id),
  CONSTRAINT subscription_user_email_fk FOREIGN KEY (user_email) REFERENCES USER (email)
);
