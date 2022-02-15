package edu.emmerson.poc.aws.assume.library.pojo;


public class CredentialsRequest {

	private String region;
	private String roleARN;
	private String roleSessionName;
	private String profileName;
	
	public CredentialsRequest() {
		super();
	}
	
	public CredentialsRequest(String region, String roleARN, String roleSessionName, String profileName) {
		this();
		this.region = region;
		this.roleARN = roleARN;
		this.roleSessionName = roleSessionName;
		this.profileName = profileName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRoleARN() {
		return roleARN;
	}

	public void setRoleARN(String roleARN) {
		this.roleARN = roleARN;
	}

	public String getRoleSessionName() {
		return roleSessionName;
	}

	public void setRoleSessionName(String roleSessionName) {
		this.roleSessionName = roleSessionName;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	@Override
	public String toString() {
		return "CredentialsRequest [region=" + region + ", roleARN=" + roleARN + ", roleSessionName=" + roleSessionName
				+ ", profileName=" + profileName + "]";
	}
	
}
