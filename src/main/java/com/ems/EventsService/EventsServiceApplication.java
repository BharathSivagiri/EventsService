package com.ems.EventsService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventsServiceApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(EventsServiceApplication.class, args);
	}
}
