package com.aws.sns;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.aws.sns.model.SnsRequestBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionRequest;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@RestController
@Log4j2
public class HelloWorld {

	@GetMapping("/")
	public String hello() {
		return "Hello World";
	}

	@PostMapping("/")
	public String processSnsMessages(@RequestHeader Map<String, String> headers, @RequestBody String requestBody) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			SnsRequestBody snsRequestBody = gson.fromJson(requestBody, SnsRequestBody.class);

			if (snsRequestBody.getType().equals("SubscriptionConfirmation")) {
				String[] topicARN = snsRequestBody.getTopicArn().split(":");
				String region = topicARN[3];

				SnsClient snsClient = SnsClient.builder().region(Region.of(region))
						.credentialsProvider(ProfileCredentialsProvider.create()).build();

				confirmSub(snsClient, snsRequestBody.getToken(), snsRequestBody.getTopicArn());
				log.info("Subscription confirmed");
				snsClient.close();
			} else if (snsRequestBody.getType().equals("Notification")) {

				log.info("=======================");
				log.info("Request Header: " + gson.toJson(headers));

				log.info("Request Body: " + requestBody.toString());
				log.info("=======================\n");
				
			}

		} catch (Exception e) {
			log.error(e.toString());
		}

		return "200 OK";

	}

	public static void confirmSub(SnsClient snsClient, String subscriptionToken, String topicArn) {

		try {
			ConfirmSubscriptionRequest request = ConfirmSubscriptionRequest.builder().token(subscriptionToken)
					.topicArn(topicArn).build();

			ConfirmSubscriptionResponse result = snsClient.confirmSubscription(request);
			log.info("\n\nStatus was " + result.sdkHttpResponse().statusCode() + "\n\nSubscription Arn: \n\n"
					+ result.subscriptionArn());

		} catch (SnsException e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			log.error(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}

}
