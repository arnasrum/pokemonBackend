package com.arnas.pokemans.controllers

import com.arnas.pokemans.dataclasses.Pokemon
import com.arnas.pokemans.repository.PokemonRepository
import com.arnas.pokemans.service.CounterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class RestController {

    @Autowired
    lateinit var pokemonRepository: PokemonRepository
    @Autowired
    lateinit var counterService: CounterService

    @GetMapping("/pokemon/{name}")
    fun getPokemon(@PathVariable name: String): Pokemon? {
        // Sanitize input from url variable
        counterService.getCounterByPokemonType(name)
        return pokemonRepository.getPokemonByName(name)
    }

    @GetMapping("/pokemon/")
    fun getAllPokemon(): MutableList<Pokemon> {
        // Sanitize input from url variable
        return pokemonRepository.getAllPokemon()
    }
}