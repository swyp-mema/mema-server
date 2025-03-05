package com.swyp.mema.global.s3;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
public class S3Service  {

	private final S3Client s3Client;
	private final String bucketName;
	private final Region region;

	public S3Service(
		@Value("${aws.s3.access-key}") String accessKey,
		@Value("${aws.s3.secret-key}") String secretKey,
		@Value("${aws.s3.region}") String region,
		@Value("${aws.s3.bucket-name}") String bucketName
	) {
		this.bucketName = bucketName;
		this.region = Region.of(region);  // Region 객체로 변환 후 저장

		this.s3Client = S3Client.builder()
			.region(this.region)
			.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
			.build();
	}

	/**
	 * 파일을 AWS S3에 업로드하고, 해당 파일의 URL 반환
	 */
	public String uploadImageFile(ByteBuffer imageBytes) {

		long currentTime = System.currentTimeMillis();
		log.info("currentTime : {}", currentTime);

		// S3 내 폴더 구조 적용 (예: store_images/ 랜덤UUID_타임스탬프.png)
		String fileName = "store_images/" + UUID.randomUUID().toString() + "_" + currentTime + ".png";

		// S3에 파일 업로드
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(fileName)
			.contentType("image/png")  // ContentType 명시
			// .acl("public-read") // S3에서 URL로 접근 가능하도록 설정
			.build();

		s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(imageBytes));

		// 업로드된 파일의 S3 URL 반환
		return "https://" + bucketName + ".s3." + region.id() + ".amazonaws.com/" + fileName;
	}
}
