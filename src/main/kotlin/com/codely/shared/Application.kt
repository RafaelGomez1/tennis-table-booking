package com.codely.shared

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories(basePackages = ["com.codely"])
@ComponentScan("com.codely")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}





