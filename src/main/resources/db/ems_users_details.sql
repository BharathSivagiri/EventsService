CREATE TABLE ems_users_details (
  id int NOT NULL AUTO_INCREMENT,
  user_name varchar(50) NOT NULL,
  user_password varchar(50) NOT NULL,
  user_email varchar(70) NOT NULL,
  user_mobile varchar(50) NOT NULL,
  user_address varchar(50) NOT NULL,
  user_account_no varchar(50) NOT NULL,
  user_type enum('ADMIN','PARTICIPANT') DEFAULT NULL,
  created_by varchar(50) NOT NULL, 
  created_date timestamp NOT NULL,
  record_status enum('ACTIVE','INACTIVE') DEFAULT NULL,
  last_updated_date timestamp NOT NULL,
  last_updated_by varchar(50) NOT NULL,
  PRIMARY KEY (id)
);

describe ems_users_details;

select * from ems_users_details;
