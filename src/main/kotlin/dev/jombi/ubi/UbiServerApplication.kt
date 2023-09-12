package dev.jombi.ubi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UbiServerApplication

fun main(args: Array<String>) {
    runApplication<UbiServerApplication>(*args)
}
