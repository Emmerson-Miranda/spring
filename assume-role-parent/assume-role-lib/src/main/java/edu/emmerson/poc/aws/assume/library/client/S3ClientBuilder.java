package edu.emmerson.poc.aws.assume.library.client;

import edu.emmerson.poc.aws.assume.library.credentials.CredentialsDelegate;
import edu.emmerson.poc.aws.assume.library.credentials.CredentialsStrategy;
import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3ClientBuilder {
	
	public static S3Client getS3Client(CredentialsRequest r) throws Exception {
		Region region = Region.of(r.getRegion());
		CredentialsDelegate cd = new CredentialsDelegate();
		CredentialsStrategy cs = cd.getCredentialsStratey();
		AwsCredentialsProvider credentialsProvider = cd.getCredentialsProvider(cs, r);
		return S3Client.builder().credentialsProvider(credentialsProvider).region(region).build();
	}

}
