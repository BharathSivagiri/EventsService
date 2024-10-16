CREATE TABLE registration_error_log (
  id int NOT NULL AUTO_INCREMENT,
  created_by varchar(50) NOT NULL,
  created_date timestamp NOT NULL,
  error_message text,
  PRIMARY KEY (id)
);

describe registration_error_log;

select * from registration_error_log;