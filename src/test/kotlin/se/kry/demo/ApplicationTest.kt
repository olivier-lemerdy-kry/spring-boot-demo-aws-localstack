package se.kry.demo

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.org.awaitility.Awaitility.await
import se.kry.demo.domain.MyMessage
import se.kry.demo.infra.LocalStackDefaults
import se.kry.demo.services.MyMessageSender
import se.kry.demo.services.StorageService
import java.time.Duration


@SpringBootTest
@Testcontainers
class ApplicationTest(
    @Value("\${app.bucket}") val bucketName: String,
    @Autowired private val publisher: MyMessageSender,
    @Autowired private val storageService: StorageService
) {

    @Test
    fun contextLoads() {
        val message = MyMessage(content = "Hello World!")
        publisher.publish(message)

        await().pollInterval(Duration.ofSeconds(2))
            .atMost(Duration.ofSeconds(10))
            .ignoreExceptions()
            .untilAsserted {
                val msg: String = storageService.downloadAsString(
                    bucketName, message.id.toString()
                )
                assertThat(msg).isEqualTo("Hello World!")
            }
    }

    companion object {
        @Container
        @JvmStatic
        val localStack = LocalStackContainer(LocalStackDefaults.DOCKER_IMAGE_NAME)
            .withServices(LocalStackContainer.Service.SQS, LocalStackContainer.Service.S3)

        @DynamicPropertySource
        @JvmStatic
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.cloud.aws.region.static") { localStack.region }
            registry.add("spring.cloud.aws.credentials.access-key") { localStack.accessKey }
            registry.add("spring.cloud.aws.credentials.secret-key") { localStack.secretKey }
            registry.add("spring.cloud.aws.sqs.endpoint") { localStack.getEndpointOverride(LocalStackContainer.Service.SQS) }
            registry.add("spring.cloud.aws.s3.endpoint") { localStack.getEndpointOverride(LocalStackContainer.Service.S3) }
        }

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", "test-queue")
            localStack.execInContainer("awslocal", "s3", "mb", "s3://test-bucket")
        }
    }

}
