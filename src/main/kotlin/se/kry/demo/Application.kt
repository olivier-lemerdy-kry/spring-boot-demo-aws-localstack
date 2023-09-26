package se.kry.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootDemoAwsLocalstackApplication

fun main(args: Array<String>) {
    runApplication<SpringBootDemoAwsLocalstackApplication>(*args)
}
