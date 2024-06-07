package com.lotto24.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringBootApplication

fun main(args: Array<String>) {
    runApplication<com.lotto24.backend.SpringBootApplication>(*args)
}
