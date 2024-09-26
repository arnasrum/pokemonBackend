package com.arnas.pokemans

import com.arnas.pokemans.dataclasses.Pokemon
import com.arnas.pokemans.repository.PokemonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class RestController {

    @Autowired
    lateinit var pokemonRepository: PokemonRepository


    @GetMapping("/pokemon/{name}")
    fun getPokemon(@PathVariable name: String): Pokemon? {
        // Sanitize input from url variable
        return pokemonRepository.getPokemonByName(name)
    }

    @GetMapping("/pokemon")
    fun getAllPokemon(): MutableList<Pokemon> {
        // Sanitize input from url variable
        return pokemonRepository.getAllPokemon()
    }
}