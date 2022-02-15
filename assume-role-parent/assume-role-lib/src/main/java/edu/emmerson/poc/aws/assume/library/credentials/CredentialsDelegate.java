package edu.emmerson.poc.aws.assume.library.credentials;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
import edu.emmerson.poc.aws.assume.library.pojo.TemporalCredential;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsAsyncClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;
import software.amazon.awssdk.utils.StringUtils;

public class CredentialsDelegate {

	private TemporalCredential loadCredentialsFromRemote(CredentialsRequest request) throws IOException {
		TemporalCredential res = null;
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
		    HttpPost httpPost = new HttpPost(getCredentialsRemoteHost());
		    httpPost.addHeader("Content-Type", "application/json");
		    
		    ObjectWriter ow = new ObjectMapper().writer();
		    String strJson = ow.writeValueAsString(request);
		    System.out.println("Request to remote server: " + strJson);
		    StringEntity strEntity = new StringEntity(strJson, ContentType.APPLICATION_JSON);
		    
		    httpPost.setEntity(strEntity);
	
		    try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
		        System.out.println(response2.getStatusLine());
		        HttpEntity entity2 = response2.getEntity();
		        // do something useful with the response body
		        // and ensure it is fully consumed
		        
		        
		        String json = new String(entity2.getContent().readAllBytes(), StandardCharsets.UTF_8);
		        EntityUtils.consume(entity2);
		        
		        System.out.println(entity2);
		        System.out.println("Raw response from remote server: " + json);
		        
		        ObjectMapper objectMapper = new ObjectMapper();
		        res = objectMapper.readValue(json, TemporalCredential.class);	
		        System.out.println("Object Response from remote server : " + res);
		    }
		}
		return res;
	}

	private TemporalCredential loadCredentialsFromLocal(CredentialsRequest request) throws ExecutionException, InterruptedException {

		ProfileCredentialsProvider devProfile = ProfileCredentialsProvider.builder().profileName(request.getProfileName()).build();

		Region region = Region.of(request.getRegion());
		StsAsyncClient stsAsyncClient = StsAsyncClient.builder().credentialsProvider(devProfile).region(region).build();

		AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder().durationSeconds(3600).roleArn(request.getRoleARN())
				.roleSessionName(request.getRoleSessionName()).build();

		Future<AssumeRoleResponse> responseFuture = stsAsyncClient.assumeRole(assumeRoleRequest);
		AssumeRoleResponse response = responseFuture.get();
		Credentials c = response.credentials();

		TemporalCredential tc = new TemporalCredential(c.accessKeyId(), c.secretAccessKey(), c.sessionToken(), c.expiration().toString());
		return tc;
	}
	
	public TemporalCredential loadCredentialsStrategy(CredentialsStrategy cs, CredentialsRequest request) throws Exception {
		final TemporalCredential res;
		switch(cs) {
			case RemoteAssumeRole:
				res = loadCredentialsFromRemote(request);
				break;
			case LocalAssumeRole:
				res = loadCredentialsFromLocal(request);
				break;
			case LocalDefaultCredentialsProvider:
				res = null;
				break;
			default:
				//Mock
				res = new TemporalCredential("mocAccessKeyId", "mockSecretAccessKey", "mockSessionToke", Instant.now().toString());
				break;
		}
		return res;
	}

	public AwsCredentialsProvider getCredentialsProvider(CredentialsStrategy cs, CredentialsRequest request) throws Exception {
		final AwsCredentialsProvider credentialsProvider;
		TemporalCredential c = loadCredentialsStrategy(cs, request);
		AwsSessionCredentials sessionCredentials = null;
		switch(cs) {
			case RemoteAssumeRole:
			case LocalAssumeRole:
				sessionCredentials = AwsSessionCredentials.create(c.getAccessKeyId(),c.getSecretAccessKey(), c.getSessionToken());
				credentialsProvider = AwsCredentialsProviderChain.builder().credentialsProviders(StaticCredentialsProvider.create(sessionCredentials)).build();
				break;
				
			default:
				credentialsProvider = DefaultCredentialsProvider.builder().build();
				break;
		}

		return credentialsProvider;
	}

	public void printCredentials(Credentials myCreds) {
		Instant exTime = myCreds.expiration();
		String tokenInfo = myCreds.sessionToken();

		// Convert the Instant to readable date
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US)
				.withZone(ZoneId.systemDefault());

		formatter.format(exTime);

		System.out.println(">>>----------------------------------------------------------");
		System.out.println("The token " + tokenInfo + "  expires on " + exTime);
		System.out.println("Access Key: " + myCreds.accessKeyId());
		System.out.println("Secret Access Key: " + myCreds.secretAccessKey());
		System.out.println("Session Token: " + myCreds.sessionToken());
		System.out.println("<<<----------------------------------------------------------");

	}
	
	public CredentialsStrategy getCredentialsStratey() {
		String cs = System.getProperty("CredentialsStratey");
		if(StringUtils.isEmpty(cs)) {
			cs = System.getenv("CredentialsStratey");
		}
		
		if(StringUtils.isEmpty(cs)) {
			cs = CredentialsStrategy.Mock.toString();
		}
		
		System.out.println("CredentialsStratey value is: " + cs);
		return CredentialsStrategy.valueOf(cs);
	}

	public String getCredentialsRemoteHost() {
		String cs = System.getProperty("CredentialsRemoteHost");
		if(StringUtils.isEmpty(cs)) {
			cs = System.getenv("CredentialsRemoteHost");
		}
		System.out.println("CredentialsRemoteHost value is: " + cs);
		return cs;
	}
	
}
