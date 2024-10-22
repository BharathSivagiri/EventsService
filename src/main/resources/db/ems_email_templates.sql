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

INSERT INTO ems_email_templates (template_name, template_code, created_by, created_date, record_status, last_updated_date, last_updated_by)
VALUES
('REGISTRATION_SUCCESS',
'<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Successful</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9; }
        .header { background-color: #4CAF50; color: white; padding: 10px; text-align: center; }
        .content { padding: 20px; background-color: white; }
        .button { display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Registration Successful!</h1>
        </div>
        <div class="content">
            <h2>Dear {userName},</h2>
            <p>We''re thrilled to confirm your registration for {eventName}!</p>
            <p>Event Details:</p>
            <ul>
                <li>Date: {eventDate}</li>
                <li>Time: {eventTime}</li>
                <li>Location: {eventLocation}</li>
            </ul>
            <p>Your registration ID is: <strong>{registrationId}</strong></p>
            <p>We can''t wait to see you at the event!</p>
            <a href="{eventDetailsLink}" class="button">View Event Details</a>
        </div>
    </div>
</body>
</html>',
'SYSTEM', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'SYSTEM'),

('EVENT_UPDATED',
'<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Event Update</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9; }
        .header { background-color: #3498db; color: white; padding: 10px; text-align: center; }
        .content { padding: 20px; background-color: white; }
        .update-box { background-color: #ecf0f1; padding: 15px; margin-top: 20px; border-left: 5px solid #3498db; }
        .button { display: inline-block; padding: 10px 20px; background-color: #3498db; color: white; text-decoration: none; border-radius: 5px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Event Update</h1>
        </div>
        <div class="content">
            <h2>Dear {userName},</h2>
            <p>We have important updates regarding the event you''re registered for: <strong>{eventName}</strong></p>
            <div class="update-box">
                <h3>Updated Event Details:</h3>
                <p>{updatedDetails}</p>
            </div>
            <p>If you have any questions about these changes, please don''t hesitate to contact us.</p>
            <a href="{eventDetailsLink}" class="button">View Updated Event Details</a>
        </div>
    </div>
</body>
</html>',
'SYSTEM', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'SYSTEM'),

('EVENT_CANCELLED',
'<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Event Cancelled</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9; }
        .header { background-color: #e74c3c; color: white; padding: 10px; text-align: center; }
        .content { padding: 20px; background-color: white; }
        .cancel-box { background-color: #fadbd8; padding: 15px; margin-top: 20px; border-left: 5px solid #e74c3c; }
        .button { display: inline-block; padding: 10px 20px; background-color: #e74c3c; color: white; text-decoration: none; border-radius: 5px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Event Cancelled</h1>
        </div>
        <div class="content">
            <h2>Dear {userName},</h2>
            <p>We regret to inform you that the following event has been cancelled:</p>
            <div class="cancel-box">
                <h3>{eventName}</h3>
                <p>Originally scheduled for: {eventDate} at {eventTime}</p>
                <p>Reason for cancellation: {cancellationReason}</p>
            </div>
            <p>We sincerely apologize for any inconvenience this may cause. If you have any questions or concerns, please contact our support team.</p>
            <a href="{refundInfoLink}" class="button">Refund Information</a>
        </div>
    </div>
</body>
</html>',
'SYSTEM', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'SYSTEM');
