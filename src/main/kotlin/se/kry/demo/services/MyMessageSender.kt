package se.kry.demo.services

import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import se.kry.demo.domain.MyMessage

@Service
class MyMessageSender(
    @Value("\${app.queue}") val queueName: String,
    val sqsTemplate: SqsTemplate
) {
    fun publish(message: MyMessage) =
        sqsTemplate.send { it.queue(queueName).payload(message) }
}