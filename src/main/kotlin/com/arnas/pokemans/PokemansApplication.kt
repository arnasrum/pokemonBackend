package com.arnas.pokemans

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class PokemansApplication

fun main(args: Array<String>) {
    val context: ApplicationContext = runApplication<PokemansApplication>(*args)
}
