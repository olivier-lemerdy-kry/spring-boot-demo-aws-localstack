package se.kry.demo

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.localstack.LocalStackContainer

@TestConfiguration(proxyBeanMethods = false)
class TestApplication {
    @Bean
    @ServiceConnection
    fun localStack() = LocalStackContainer()
}

fun main(args: Array<String>) {
    fromApplication<Application>().with(TestApplication::class).run(*args)
}
