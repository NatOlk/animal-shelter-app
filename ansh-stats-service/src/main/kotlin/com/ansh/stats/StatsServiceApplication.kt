package com.ansh.stats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.ansh.stats.repository"])
@EntityScan(basePackages = ["com.ansh.stats.entity"])
class StatsServiceApplication

fun main(args: Array<String>) {
    runApplication<StatsServiceApplication>(*args)
}