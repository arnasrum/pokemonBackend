package com.arnas.pokemans.dataclasses

data class Pokemon(
    val name: String, val pokedexID: Int,
    val type1: Int, val type2: Int, val hp: Int,
    val attack: Int, val defense: Int,
    val specialAttack: Int, val specialDefense: Int,
    val speed: Int, val spriteFront: String?, val spriteBack: String?
)
