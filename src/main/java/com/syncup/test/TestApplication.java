package com.syncup.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.syncup.test.graph.*;
import com.syncup.test.graph.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class TestApplication implements CommandLineRunner {

	private final static String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IlZUdndPQ3h2NkUyTGZMQmRPa3RKRmVmYkdEcWlxNDR2eDNqMDZkQWRLU0kiLCJhbGciOiJSUzI1NiIsIng1dCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCIsImtpZCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCJ9.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTAwMDAtYzAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC85OWQ1NWZmMS1mYmE2LTRjODQtYjJmYi01MDE4Mzc4NTgxMmQvIiwiaWF0IjoxNjM0Mjk5MTA2LCJuYmYiOjE2MzQyOTkxMDYsImV4cCI6MTYzNDMwMzAwNiwiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkUyWmdZTGpvWmVoVWsxR3RFTlh5SWVPcDZRS08rOHIrSXZ2U2RXM2FOOVg4a1ZuaTNRNEEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6IlBlb3BsZU1hZ25hIiwiYXBwaWQiOiJkNjViNmIzNC0wMGJlLTRiMDYtOWY4NS1mNjdjMDllNzRlYmUiLCJhcHBpZGFjciI6IjAiLCJmYW1pbHlfbmFtZSI6Ikphbmt1IiwiZ2l2ZW5fbmFtZSI6IkpvYW4iLCJpZHR5cCI6InVzZXIiLCJpcGFkZHIiOiI4MC43OC43Mi4xMzUiLCJuYW1lIjoiSm9hbiBKYW5rdSIsIm9pZCI6IjY3MDg0MTJlLTE5YjUtNGE1YS1hM2U5LTFjZWIwNmI4YzNmZiIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzMjAwMTVGODI5QTJGIiwicmgiOiIwLkFRc0E4Vl9WbWFiN2hFeXktMUFZTjRXQkxUUnJXOWEtQUFaTG40WDJmQW5uVHI0TEFMWS4iLCJzY3AiOiJDYWxlbmRhcnMuUmVhZCBDYWxlbmRhcnMuUmVhZC5TaGFyZWQgQ2FsZW5kYXJzLlJlYWRXcml0ZSBDb250YWN0cy5SZWFkIENvbnRhY3RzLlJlYWQuU2hhcmVkIGVtYWlsIG9wZW5pZCBQZW9wbGUuUmVhZCBQcmVzZW5jZS5SZWFkIFByZXNlbmNlLlJlYWQuQWxsIHByb2ZpbGUgVGFza3MuUmVhZFdyaXRlIFVzZXIuUmVhZCBVc2VyLlJlYWRCYXNpYy5BbGwgVXNlci5SZWFkV3JpdGUiLCJzdWIiOiI1YkFPWXJfQWg4R0p6MDJJOWl3dEdBVGJSYzRiTnB6NDFYdW5xNTFEUk04IiwidGVuYW50X3JlZ2lvbl9zY29wZSI6IkVVIiwidGlkIjoiOTlkNTVmZjEtZmJhNi00Yzg0LWIyZmItNTAxODM3ODU4MTJkIiwidW5pcXVlX25hbWUiOiJqb2FuLmphbmt1QGlrdWJpbmZvLmFsIiwidXBuIjoiam9hbi5qYW5rdUBpa3ViaW5mby5hbCIsInV0aSI6ImJoeVNuYmthamsyZnFkTlhCUnROQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdLCJ4bXNfc3QiOnsic3ViIjoicjg1SGk0eVVzYXVPZnJCUTFiRkdOSlZha2ZsWlNhekNXVFEtcHhveVlNayJ9LCJ4bXNfdGNkdCI6MTQxNjk5MjI4OH0.RKoX7VyIcskt4rvlveTL1xrxqYYz4t4GbD4wJLTwRZTR1DvViUmERc0FB7SyhwDWsuQ4v4L4-anPUBOiiYfHEmru3gysHwwHy97MyaEtX-Oizlq7MJJesUgyHA8EuEtMuUKz7qYwIVFA_IgD0HQi9zcpECzb8ZPRJtiKgmWKzPMqvFWjje9rJb0676ymfGy06U4MEz5GdWmOYhUm7i8pqMhQowdcdN6U292YiUgcaQ3T5G98eaG3-_RKmspEygwibGBvjTN9ph8B1e8zkx2NHt5I9g2PZHhoY1FFtZnMeCV4cvd8Yn67IjSa-A7C8xSaPndPOopLyEvFs_YPXjhfWg";

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		WebClient webClient = webClientForGraph();

			String response = webClient
					.get()
					.retrieve()
					.bodyToMono(String.class)
					.block();

			System.out.println(response);

			int beginnig = response.indexOf("value\":[{\"id\":");

			System.out.println("Beginning "+beginnig);

			int lastIndex = response.indexOf(",\"name\":\"Calendar\",");
			System.out.println("Ending "+lastIndex);

			String calendarId = response.substring(beginnig+15,lastIndex-1);
			System.out.println("Extracted Calendar ID is "+calendarId);

			Date start = new Date();
			Date end = new Date();

			start.setDateTime(LocalDateTime.now().plusMinutes(16L).minusHours(9L));
			start.setTimeZone("Pacific Standard Time");

			end.setDateTime(LocalDateTime.now().plusMinutes(40L).minusHours(9L));
			end.setTimeZone("Pacific Standard Time");

			Body body = new Body();
			body.setContent("<h1>CONTENT TEST</h1>");
			body.setContentType("HTML");


//			EmailAddress emailAddress = new EmailAddress();
//			emailAddress.setAddress("fabiola.alterziu@ikubinfo.al");
//			emailAddress.setName("Fabiola");

			EmailAddress emailAddress2 = new EmailAddress();
			emailAddress2.setAddress("joan.janku@ikubinfo.al");
			emailAddress2.setName("JOAN");

//			Attendee attendees = new Attendee();
//			attendees.setEmailAddress(emailAddress);

			Attendee attendee1 = new Attendee();
			attendee1.setEmailAddress(emailAddress2);
			attendee1.setType("Required");
	//		attendees.setType("Required");

			List<Attendee> attenders = new ArrayList<>();
			//attenders.add(attendees);
			attenders.add(attendee1);

			boolean isOnlineMeeting = true;

			GraphCalendarSchedule graphCalendarSchedule = new GraphCalendarSchedule();
			graphCalendarSchedule.setStart(start);
			graphCalendarSchedule.setEnd(end);
			graphCalendarSchedule.setAttendees(attenders);
			graphCalendarSchedule.setBody(body);
			graphCalendarSchedule.setSubject("TEST 3");

			graphCalendarSchedule.setOnlineMeeting(isOnlineMeeting);

			System.out.println("JSON VALUE IS ");
			System.out.println(objectMapper().writeValueAsString(graphCalendarSchedule));

			String response2 = webClient.post()
					.uri(calendarId+"/events")
					.contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(graphCalendarSchedule),GraphCalendarSchedule.class)
							.retrieve()
					.bodyToMono(String.class).block();
			int beginIndexOfIdOfEvent = response2.indexOf("\"id\":\"") +6;
			int lastIndexOfIdOfEvent = response2.indexOf("\",\"createdDateTime\"");
			String idOfEvent = response2.substring(beginIndexOfIdOfEvent,lastIndexOfIdOfEvent);
			System.out.println(response2);
			System.out.println("ID IS "+idOfEvent);
	}

	@Bean
	public WebClient webClientForGraph(){
		return WebClient.builder()
				.baseUrl("https://graph.microsoft.com/v1.0/me/calendars/")
				.defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer "+token)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}
}
