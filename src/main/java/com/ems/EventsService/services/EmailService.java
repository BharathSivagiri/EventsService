package com.ems.EventsService.services;

public interface EmailService
{
    void sendHtmlMessage(String to, String subject, String htmlContent);
}
