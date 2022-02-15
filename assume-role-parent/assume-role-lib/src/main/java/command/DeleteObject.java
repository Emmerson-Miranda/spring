//snippet-sourcedescription:[DeleteObjects.java demonstrates how to delete multiple objects from an Amazon Simple Storage Service (Amazon S3) bucket.]
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

import java.util.ArrayList;
// snippet-end:[s3.java2.delete_objects.import]

import edu.emmerson.poc.aws.assume.library.client.S3ClientBuilder;
import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
import software.amazon.awssdk.services.s3.S3Client;
// snippet-start:[s3.java2.delete_objects.import]
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * To run this AWS code example, ensure that you have setup your development environment, including your AWS credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */

public class DeleteObject {

    public static void main(String[] args) {
    	DeleteObject d = new DeleteObject();
    	d.delete(args);
    }

    public void delete(String[] args) {
    	
		final String USAGE = "\n" + "Usage:\n"
				+ "    <regionName> <bucketName> <roleARN> <roleSessionName> <profileName> <keyName> <path>\n\n"
				+ "Where:\n" 
				+ "    regionName      - Bucket region \n\n" 
				+ "    bucketName      - the Amazon S3 bucket from which objects are read. \n\n"
				+ "    roleARN         - roleARN to assume \n\n"
				+ "    roleSessionName - roleSession name (free text) \n\n"
				+ "    profileName     - profile name(free text)  \n\n"
				+ "    keyName - the key name. \n\n" ;


        if (args.length != 6) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String regionName = args[0];
		String bucketName = args[1];

		String roleARN = args[2];
		String roleSessionName = args[3];
		String profileName = args[4];

		String keyName = args[5];

		
        System.out.println("Deleting an object from the Amazon S3 bucket: " + bucketName);

        
		try {

			CredentialsRequest r = new CredentialsRequest(regionName, roleARN, roleSessionName, profileName);
			S3Client s3Client = S3ClientBuilder.getS3Client(r);
			
			deleteBucketObjects(s3Client, bucketName, keyName);
			
			s3Client.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

        

    }
    
    // snippet-start:[s3.java2.delete_objects.main]
    public void deleteBucketObjects(S3Client s3, String bucketName, String objectName) {

        ArrayList<ObjectIdentifier> toDelete = new ArrayList<ObjectIdentifier>();
        toDelete.add(ObjectIdentifier.builder().key(objectName).build());

        try {
            DeleteObjectsRequest dor = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder().objects(toDelete).build())
                    .build();
            s3.deleteObjects(dor);
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }
   // snippet-end:[s3.java2.delete_objects.main]
}
