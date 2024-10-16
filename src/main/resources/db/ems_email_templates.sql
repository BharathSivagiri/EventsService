CREATE TABLE ems_email_templates (
  id int NOT NULL AUTO_INCREMENT,
  template_name varchar(20) NOT NULL,
  template_code text,
  created_by varchar(50) NOT NULL, 
  created_date timestamp NOT NULL,
  record_status enum('ACTIVE','INACTIVE') DEFAULT NULL,
  last_updated_date timestamp NOT NULL,
  last_updated_by varchar(50) NOT NULL,
  PRIMARY KEY (id)
);

describe ems_email_service;

select * from ems_email_service;