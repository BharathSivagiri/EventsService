CREATE TABLE ems_events_registration (
  id int NOT NULL AUTO_INCREMENT,
  event_id int DEFAULT NULL,
  user_id int DEFAULT NULL,
  registration_status enum('REGISTERED','CANCELLED') DEFAULT NULL,
  transaction_id varchar(20) NOT NULL,
  created_by varchar(50) NOT NULL, 
  created_date timestamp NOT NULL,
  record_status enum('ACTIVE','INACTIVE') DEFAULT NULL,
  last_updated_date timestamp NOT NULL,
  last_updated_by varchar(50) NOT NULL,
  PRIMARY KEY (id),
);

describe ems_events_registration;

select * from ems_events_registration;