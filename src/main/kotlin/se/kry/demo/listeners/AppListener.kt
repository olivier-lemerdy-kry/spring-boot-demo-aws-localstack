package se.kry.demo.listeners

import io.awspring.cloud.sqs.annotation.SqsListener

class AppListener {

    @SqsListener("#{app.queue}")
    fun listen(message: String) {
        println(message)
    }
}