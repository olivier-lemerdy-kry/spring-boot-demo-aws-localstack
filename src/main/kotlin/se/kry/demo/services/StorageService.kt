package se.kry.demo.services

import io.awspring.cloud.s3.S3Template
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class StorageService(
    private val s3Template: S3Template
) {

    fun upload(bucketName: String, key: String, stream: InputStream) =
        s3Template.upload(bucketName, key, stream)

    fun download(bucketName: String, key: String): InputStream =
        s3Template.download(bucketName, key).inputStream

    fun downloadAsString(bucketName: String, key: String) =
        download(bucketName, key)
            .use { String(it.readAllBytes()) }
}