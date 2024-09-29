package com.arnas.pokemans.controllers

import com.arnas.pokemans.dataclasses.Pokemon
import com.arnas.pokemans.service.CounterService
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RequestMapping("/counter")
@RestController
class CounterController(private val counterService: CounterService) {


    @GetMapping("/{pokemon}")
   fun getCounterByName(@PathVariable("pokemon") pokemon: String): MutableList<Pokemon> {
        return counterService.getCounterByPokemonType(pokemon)
   }

}