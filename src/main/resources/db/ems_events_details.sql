CREATE TABLE ems_events_details (
  id int NOT NULL AUTO_INCREMENT,
  event_name varchar(50) NOT NULL,
  event_description varchar(100) NOT NULL,
  event_date date NOT NULL,
  event_location varchar(50) NOT NULL,
  event_capacity int NOT NULL,
  event_fee double NOT NULL,
  event_status enum('OPENED','CLOSED','CANCELLED') DEFAULT NULL,
  created_by varchar(50) NOT NULL, 
  created_date timestamp NOT NULL,
  record_status enum('ACTIVE','INACTIVE') DEFAULT NULL,
  last_updated_date timestamp NOT NULL,
  last_updated_by varchar(50) NOT NULL,
  PRIMARY KEY (id)
);


describe ems_events_details;

select * from ems_events_details;