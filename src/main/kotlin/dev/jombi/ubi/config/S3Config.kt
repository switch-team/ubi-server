package dev.jombi.ubi.config

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config(@Value("cloud.aws.region.static") val region: String) {
    @Bean
    fun amazonS3Client(): AmazonS3Client {
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .build() as AmazonS3Client
    }
}