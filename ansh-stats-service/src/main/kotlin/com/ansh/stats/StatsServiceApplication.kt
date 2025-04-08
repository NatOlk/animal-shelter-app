package com.ansh.stats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StatsServiceApplication

fun main(args: Array<String>) {
    runApplication<StatsServiceApplication>(*args)
}