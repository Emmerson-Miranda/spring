package command;

import java.util.List;
import java.util.ListIterator;

import edu.emmerson.poc.aws.assume.library.client.S3ClientBuilder;
import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

//Source: https://gist.github.com/joelforjava/21c932111e21b73b40cb3d6dc63555d0
public class ListObject {

	public static void main(String[] args) {
		ListObject lbc = new ListObject();
		lbc.list(args);
	}
	
	public ListObject() {
	}
	
	public void list(String[] args) {
		final String USAGE = "\n" 
				+ "Usage:\n"
				+ "    <regionName> <bucketName> <roleARN> <roleSessionName> <profileName> \n\n" 
				+ "Where:\n"
				+ "    regionName      - Bucket region \n\n" 
				+ "    bucketName      - the Amazon S3 bucket from which objects are read. \n\n"
				+ "    roleARN         - roleARN to assume \n\n"
				+ "    roleSessionName - roleSession name (free text) \n\n"
				+ "    profileName     - profile name(free text)  \n\n"
				;

		if (args.length != 5) {
			System.out.println(USAGE);
			System.exit(1);
		}
		String regionName = args[0];
		String bucketName = args[1];
		String roleARN = args[2];
		String roleSessionName = args[3];
		String profileName = args[4];

		try {

			CredentialsRequest r = new CredentialsRequest(regionName, roleARN, roleSessionName,profileName);
			S3Client s3Client = S3ClientBuilder.getS3Client(r);

			listBucketObjects(s3Client, bucketName);
			s3Client.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 		
	}


	@SuppressWarnings("rawtypes")
	private void listBucketObjects(S3Client s3, String bucketName) {

		try {
			ListObjectsRequest listObjects = ListObjectsRequest.builder().bucket(bucketName).build();

			ListObjectsResponse res = s3.listObjects(listObjects);
			List<S3Object> objects = res.contents();
			
			System.out.println("");
			System.out.println(">>>----------------------------------------------------------");

			System.out.printf("No. of Objects: %d", objects.size());

			for (ListIterator iterVals = objects.listIterator(); iterVals.hasNext();) {
				S3Object myValue = (S3Object) iterVals.next();
				System.out.print("\n The name of the key is " + myValue.key());

			}
			System.out.println("");
			System.out.println("<<<----------------------------------------------------------");

		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}

}
