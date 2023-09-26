package se.kry.demo.listeners

import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.beans.factory.annotation.Value
import se.kry.demo.domain.MyMessage
import se.kry.demo.services.StorageService

class MyMessageListener(
    @Value("\${app.bucket}") val bucketName: String,
    val storageService: StorageService
) {

    @SqsListener("#{app.queue}")
    fun listen(message: MyMessage) {
        this.storageService.upload(bucketName, message.id.toString(), message.content.byteInputStream())
    }
}