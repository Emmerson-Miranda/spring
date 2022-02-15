//snippet-sourcedescription:[PutObject.java demonstrates how to upload an object to an Amazon Simple Storage Service (Amazon S3) bucket.]
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// snippet-end:[s3.java2.s3_object_upload.import]

import edu.emmerson.poc.aws.assume.library.client.S3ClientBuilder;
import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
// snippet-start:[s3.java2.s3_object_upload.import]
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * To run this AWS code example, ensure that you have setup your development
 * environment, including your AWS credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */

public class PutObject {

	public static void main(String[] args) {
		PutObject po = new PutObject();
		po.put(args);
	}

	public void put(String[] args) {
		final String USAGE = "\n" + "Usage:\n"
				+ "  <regionName> <bucketName> <roleARN> <roleSessionName> <profileName> <objectKey> <objectPath> \n\n"
				+ "Where:\n" + "    regionName      - Bucket region \n\n"
				+ "    bucketName      - the Amazon S3 bucket from which objects are read. \n\n"
				+ "    roleARN         - roleARN to assume \n\n"
				+ "    roleSessionName - roleSession name (free text) \n\n"
				+ "    profileName     - profile name(free text)  \n\n"
				+ "    objectKey - the object to upload (for example, book.pdf).\n"
				+ "    objectPath - the path where the file is located (for example, C:/AWS/book2.pdf). \n\n";

		if (args.length != 3) {
			System.out.println(USAGE);
			System.exit(1);
		}

		String regionName = args[0];
		String bucketName = args[1];

		String roleARN = args[2];
		String roleSessionName = args[3];
		String profileName = args[4];

		String objectKey = args[5];
		String objectPath = args[6];

		System.out.println("Putting object " + objectKey + " into bucket " + bucketName);
		System.out.println("  in bucket: " + bucketName);

		try {
			
			CredentialsRequest r = new CredentialsRequest(regionName, roleARN, roleSessionName, profileName);
			S3Client s3Client = S3ClientBuilder.getS3Client(r);

			String result = putS3Object(s3Client, bucketName, objectKey, objectPath);
			System.out.println("Tag information: " + result);
			s3Client.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// snippet-start:[s3.java2.s3_object_upload.main]
	public String putS3Object(S3Client s3, String bucketName, String objectKey, String objectPath) {

		try {

			Map<String, String> metadata = new HashMap<>();
			metadata.put("x-amz-meta-myVal", "test");

			PutObjectRequest putOb = PutObjectRequest.builder().bucket(bucketName).key(objectKey).metadata(metadata)
					.build();

			PutObjectResponse response = s3.putObject(putOb, RequestBody.fromBytes(getObjectFile(objectPath)));

			return response.eTag();

		} catch (S3Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return "";
	}

	// Return a byte array
	private static byte[] getObjectFile(String filePath) {

		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;

		try {
			File file = new File(filePath);
			bytesArray = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytesArray);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytesArray;
	}
	// snippet-end:[s3.java2.s3_object_upload.main]
}