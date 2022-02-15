package edu.emmerson.poc.aws.assume.library.commandline;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import command.ListObject;

class ListBucketContentTest {

	@Test
	void test() {
		String[] args = {"eu-west-2","arn:aws:s3:eu-west-2:123456789012:accesspoint/project2",
						"arn:aws:iam::123456789012:role/DEMO_S3_ACCESS_POINT_PROJECT1", 
						"mysession", 
						"myprofile"};
		System.out.println("Starting test");
		
		System.setProperty("CredentialsStratey", "RemoteAssumeRole");
		System.setProperty("CredentialsRemoteHost", "http://localhost:8080/credentials");
		
		ListObject lbc = new ListObject();
		lbc.list(args);
		
		assertTrue(true);
	}

}
