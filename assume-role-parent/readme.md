# PoC - AWS - AssumeRole

This is a PoC that shows how to use AWS assumeRole and STS

## Credits
The code in this PoC is based on other projects listed below.

- [AWS SDK Examples - S3](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/s3)
- [STS Call example - AssumeRole](https://gist.github.com/joelforjava/21c932111e21b73b40cb3d6dc63555d0)

## Parameters
There are a couple of parameters to configure the behaviour of the library and microservice

### CredentialsStratey 
java property or environment variable, possible values:

- LocalDefaultCredentialsProvider (use AWS credentials from the environment)
- LocalAssumeRole - Direct call to STS service to get Temporal AWS Credentials
- RemoteAssumeRole - Call assume-role-microservice to get Temporal AWS Credentials.
- Mock (use AWS credentials from the environment)

### CredentialsRemoteHost
java property or environment variable

Remote host address (assume-role-microservice), required when CredentialsStratey=RemoteAssumeRole, e.g: http://localhost:8080/credentials


## Artifacts
This project generate two artifacts.

### assume-role-lib-1.0-SNAPSHOT-jar-with-dependencies.jar
AWS S3 client with following commands:

- command.ListObject
- command.PutObject
- command.GetObject
- command.DeleteObject

Example:

```
$ java -cp assume-role-lib-1.0-SNAPSHOT-jar-with-dependencies.jar command.ListObject eu-west-2 arn:aws:s3:eu-west-2:123456789012:accesspoint/project2 arn:aws:iam::123456789012:role/DEMO_S3_ACCESS_POINT_PROJECT2 mysession demoprofile

>>>----------------------------------------------------------
No. of Objects: 5
 The name of the key is dev/
 The name of the key is dev/programme1/
 The name of the key is dev/programme1/readme.txt
 The name of the key is dev/programme2/
 The name of the key is dev/programme2/readme.txt
<<<----------------------------------------------------------
```

```
$ java -cp assume-role-lib-1.0-SNAPSHOT-jar-with-dependencies.jar command.GetObject eu-west-2 arn:aws:s3:eu-west-2:123456789012:accesspoint/project2 arn:aws:iam::123456789012:role/DEMO_S3_ACCESS_POINT_PROJECT2 mysession demoprofile dev/programme2/readme.txt ./readme.txt
```

### assume-role-microservice-1.0-SNAPSHOT.jar

This microservice call AWS STS service to get temporal permissions.

```
$ curl -X POST -d '{"region":"eu-west-2","roleARN":"roleARN","roleSessionName":"roleSessionName","profileName":"profileName"}' -H "Content-Type: application/json" localhost:8080/credentials

{"accessKeyId":"mocAccessKeyId","secretAccessKey":"mockSecretAccessKey","sessionToken":"mockSessionToke","expiration":"2022-02-15T23:04:39.932452Z"}
```

Starting the microservice 

```
$ java -jar assume-role-microservice-1.0-SNAPSHOT.jar
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/home/ec2-user/assume-role-microservice-1.0-SNAPSHOT.jar!/BOOT-INF/lib/logback-classic-1.2.3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/home/ec2-user/assume-role-microservice-1.0-SNAPSHOT.jar!/BOOT-INF/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [ch.qos.logback.classic.util.ContextSelectorStaticBinder]

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.3.RELEASE)

...
```


