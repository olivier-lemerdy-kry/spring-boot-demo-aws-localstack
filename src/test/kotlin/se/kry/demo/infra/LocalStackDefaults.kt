package se.kry.demo.infra

import org.testcontainers.utility.DockerImageName

object LocalStackDefaults {
    const val VERSION = "2.2"
    val DEFAULT_IMAGE_NAME: DockerImageName by lazy { DockerImageName.parse("localstack/localstack") }
    val DOCKER_IMAGE_NAME: DockerImageName by lazy { DEFAULT_IMAGE_NAME.withTag(VERSION) }
}