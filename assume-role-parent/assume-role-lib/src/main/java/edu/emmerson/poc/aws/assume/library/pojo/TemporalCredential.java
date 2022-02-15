package edu.emmerson.poc.aws.assume.library.pojo;

public class TemporalCredential {

    private String accessKeyId;

    private String secretAccessKey;

    private String sessionToken;

    private String expiration;

    public TemporalCredential() {
		super();
	}
    

	public TemporalCredential(String accessKeyId, String secretAccessKey, String sessionToken, String expiration) {
		this();
		this.accessKeyId = accessKeyId;
		this.secretAccessKey = secretAccessKey;
		this.sessionToken = sessionToken;
		this.expiration = expiration;
	}


	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	@Override
	public String toString() {
		return "TemporalCredential [accessKeyId=" + accessKeyId + ", secretAccessKey=" + secretAccessKey
				+ ", sessionToken=" + sessionToken + ", expiration=" + expiration + "]";
	}
    
    
    
}
