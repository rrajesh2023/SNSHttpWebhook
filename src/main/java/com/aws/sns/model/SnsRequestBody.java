package com.aws.sns.model;

import lombok.Data;

@Data
public class SnsRequestBody {
	
	private String Type;
	private String MessageId;
	private String Token;
	private String TopicArn;
	private String Message;
	private String SubscribeURL;
	private String Timestamp;
	private String SignatureVersion;
	private String Signature;
	private String SigningCertURL;
	private String UnsubscribeURL;
	
}
