package com.arnas.pokemans

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PokemansApplication

fun main(args: Array<String>) {
    runApplication<PokemansApplication>(*args)
    Thread.sleep(1000)
}
