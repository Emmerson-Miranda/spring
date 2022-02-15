package edu.emmerson.poc.aws.assume.library.credentials;

public enum CredentialsStrategy {
	/**
	 * Use default DefaultCredentialsProvider cdlass to load AWS credentials.
	 */
	LocalDefaultCredentialsProvider,
	/**
	 * Load AWS Credentials calling AWS STS service to get temporal access.
	 */
	LocalAssumeRole,
	/**
	 * Make a call to an external (HTTP) service to get temporal credentials to get temporal access.
	 */
	RemoteAssumeRole,
	/**
	 * Dummy values to test using mock implementation.
	 */
	Mock
}
