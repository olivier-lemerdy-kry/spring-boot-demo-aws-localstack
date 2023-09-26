package se.kry.demo.domain

import java.util.*

data class MyMessage(
    val id: UUID = UUID.randomUUID(),
    val content: String
)
