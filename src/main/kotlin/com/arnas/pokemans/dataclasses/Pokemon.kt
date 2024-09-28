package com.arnas.pokemans.dataclasses

data class PokemonType(val type1: String, val type2: String?)

data class PokemonSprites(val defaultFront: String?, val defaultBack: String?)

data class PokemonStats(val hp: Int, val attack: Int, val defense: Int,
                        val specialAttack: Int, val specialDefense: Int,
                        val speed: Int)

data class Pokemon(
    val name: String, val pokedexID: Int,
    val type: PokemonType, val stats: PokemonStats,
    val sprites: PokemonSprites
)
