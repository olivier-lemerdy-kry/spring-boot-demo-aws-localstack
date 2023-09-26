package se.kry.demo

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import se.kry.demo.infra.LocalStackDefaults

@SpringBootTest
@Testcontainers
class ApplicationTest {

    @Test
    fun contextLoads() {
    }

    companion object {
        @Container
        @JvmStatic
        val localStack = LocalStackContainer(LocalStackDefaults.DOCKER_IMAGE_NAME)
            .withServices(LocalStackContainer.Service.SQS)

        @DynamicPropertySource
        @JvmStatic
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.cloud.aws.region.static") { localStack.region }
            registry.add("spring.cloud.aws.credentials.access-key") { localStack.accessKey }
            registry.add("spring.cloud.aws.credentials.secret-key") { localStack.secretKey }
            registry.add("spring.cloud.aws.sqs.endpoint") { localStack.getEndpointOverride(LocalStackContainer.Service.SQS) }
        }

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", "test-queue")
        }
    }

}
