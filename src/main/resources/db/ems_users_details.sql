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

INSERT INTO ems_users_details (user_name, user_password, user_email, user_mobile, user_address, user_account_no, user_type, created_by, created_date, record_status, last_updated_date, last_updated_by)
VALUES
('John Doe', 'password123', 'goodbookscontact24@gmail.com', '1234567890', '123 Main St, City', 'ACC001', 'ADMIN', 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System'),
('Jane Smith', 'securepass', 'rock90247@gmail.com', '9876543210', '456 Elm St, Town', 'ACC002', 'PARTICIPANT', 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System'),
('Mike Johnson', 'mikepass', 'rock90247@gmail.com', '5555555555', '789 Oak Ave, Village', 'ACC003', 'PARTICIPANT', 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System'),
('Sarah Brown', 'sarahsecret', 'rock90247@gmail.com', '1112223333', '321 Pine Rd, County', 'ACC004', 'PARTICIPANT', 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System'),
('Alex Wilson', 'alexpass', 'goodbookscontact24@gmail.com', '4444444444', '654 Cedar Ln, State', 'ACC005', 'ADMIN', 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System');
