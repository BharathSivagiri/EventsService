CREATE TABLE ems_auth_token (
  id int NOT NULL AUTO_INCREMENT,
  user_id_auth int DEFAULT NULL,
  creation_time datetime NOT NULL,
  valid_for int NOT NULL,
  reset_time datetime NOT NULL,
  auth_token varchar(50) NOT NULL,
  created_by varchar(50) NOT NULL, 
  created_date timestamp NOT NULL,
  record_status enum('ACTIVE','INACTIVE') DEFAULT NULL,
  last_updated_date timestamp NOT NULL,
  last_updated_by varchar(50) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT user_id_auth FOREIGN KEY (user_id_auth) REFERENCES ems_users_details (id)
);

describe ems_auth_token;

select * from ems_auth_token;