package com.aws.sns;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class HelloWorld {


	@GetMapping("/")
	public String hello() {
		return "Hello World";
	}

	@PostMapping("/")
	public String getEventFromSns(@RequestHeader Map<String, String> headers, @RequestBody String requestBody) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String requestHeader = gson.toJson(headers);
			log.info("=======================");
			log.info("Request Header: " + requestHeader);
			// System.out.println("Request Body: " + requestBody);
			log.info("Request Body: " + requestBody);
			log.info("=======================\n");
		} catch (Exception e) {
			log.error(e.toString());
		}

		return "200 OK";

	}

}
