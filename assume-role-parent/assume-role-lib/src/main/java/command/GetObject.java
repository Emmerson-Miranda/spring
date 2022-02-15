// snippet-comment:[These are tags for the AWS doc team's sample catalog. Do not remove.]
// snippet-sourcedescription:[GetObjectData.java demonstrates how to read data from an Amazon Simple Storage Service (Amazon S3) object.]
//snippet-keyword:[AWS SDK for Java v2]
//snippet-keyword:[Code Sample]
//snippet-service:[Amazon S3]
//snippet-sourcetype:[full-example]
//snippet-sourcedate:[09/27/2021]
//snippet-sourceauthor:[scmacdon-aws]

/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/

package command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
// snippet-end:[s3.java2.getobjectdata.import]

import edu.emmerson.poc.aws.assume.library.client.S3ClientBuilder;
import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
// snippet-start:[s3.java2.getobjectdata.import]
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * To run this AWS code example, ensure that you have setup your development
 * environment, including your AWS credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */

public class GetObject {

	public static void main(String[] args) {
		GetObject go = new GetObject();
		go.getObject(args);
	}
	
	public GetObject() {
	}

	public void getObject(String[] args) {

		final String USAGE = "\n" + "Usage:\n"
				+ "    <regionName> <bucketName> <roleARN> <roleSessionName> <profileName> <keyName> <path>\n\n"
				+ "Where:\n" 
				+ "    regionName      - Bucket region \n\n" 
				+ "    bucketName      - the Amazon S3 bucket from which objects are read. \n\n"
				+ "    roleARN         - roleARN to assume \n\n"
				+ "    roleSessionName - roleSession name (free text) \n\n"
				+ "    profileName     - profile name(free text)  \n\n"
				+ "    keyName - the key name. \n\n" 
				+ "    path - the path where the file is written to. \n\n";

		if (args.length != 7) {
			System.out.println(USAGE);
			System.exit(1);
		}

		String regionName = args[0];
		String bucketName = args[1];

		String roleARN = args[2];
		String roleSessionName = args[3];
		String profileName = args[4];

		String keyName = args[5];
		String path = args[6];

		try {
			CredentialsRequest r = new CredentialsRequest(regionName, roleARN, roleSessionName, profileName);
			S3Client s3Client = S3ClientBuilder.getS3Client(r);

			getObjectBytes(s3Client, bucketName, keyName, path);
			s3Client.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// snippet-start:[s3.java2.getobjectdata.main]
	public void getObjectBytes(S3Client s3, String bucketName, String keyName, String path) {

		try {
			GetObjectRequest objectRequest = GetObjectRequest.builder().key(keyName).bucket(bucketName).build();

			ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
			byte[] data = objectBytes.asByteArray();

			// Write the data to a local file
			File myFile = new File(path);
			OutputStream os = new FileOutputStream(myFile);
			os.write(data);
			System.out.println("Successfully obtained bytes from an S3 object");
			os.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}
	// snippet-end:[s3.java2.getobjectdata.main]
}
