INSERT INTO USERS (EMAIL, PASSWORD) VALUES ('test@test.com', 'test');
INSERT INTO USERS (EMAIL, PASSWORD) VALUES ('test2@test.com', 'test');
INSERT INTO SUBSCRIPTION (USER_EMAIL, SERVICE_ID, SERVICE_NAME, LANGUAGE_ID, LANGUAGE_NAME, SEARCH_UNTIL_DATE) VALUES
  ('test@test.com', '1', 'Doctor', '1', 'Spanish', to_date('14-02-2018', 'dd-mm-yyyy'));
