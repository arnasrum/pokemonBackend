package com.arnas.pokemans.service

import com.arnas.pokemans.dataclasses.Pokemon
import com.arnas.pokemans.repository.PokemonRepository
import com.arnas.pokemans.repository.TypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.max

@Service
class CounterService {

    @Autowired
    lateinit var pokemonRepository : PokemonRepository
    @Autowired
    lateinit var typeRepository: TypeRepository

    fun getCounterByPokemonType(name: String): MutableList<Pokemon> {

        val counters: MutableList<Pokemon> = mutableListOf()
        val targetPokemon: Pokemon = pokemonRepository.getPokemonByName(name) ?: return counters

        val superEffectiveTypes: MutableList<Int> = mutableListOf()
        val effectiveTypes: MutableList<Int> = mutableListOf()
        val resistTypes: MutableList<Int> = mutableListOf()

        typeRepository.getCounterTypes(targetPokemon.type1).forEach { typeDamagePair ->
            if(typeDamagePair.second == 2F) {
                effectiveTypes.add(typeDamagePair.first)
            } else {
                resistTypes.add(typeDamagePair.first)
            }
        }
        if(targetPokemon.type2 != null) {
            typeRepository.getCounterTypes(targetPokemon.type2).forEach { typeDamagePair ->
                if(typeDamagePair.second == 2F) {
                    if(effectiveTypes.contains(typeDamagePair.first)) {
                        effectiveTypes.remove(typeDamagePair.first)
                        superEffectiveTypes.add(typeDamagePair.first)
                        //effectiveTypes.addFirst(typeDamagePair.first)
                    } else {
                        effectiveTypes.addLast(typeDamagePair.first)
                    }
                } else {
                    resistTypes.add(typeDamagePair.first)
                }
            }
        }
        resistTypes.forEach{ type ->
            if(effectiveTypes.contains(type)) {
                effectiveTypes.remove(type)
            }
        }
        val potentialPokemon: MutableList<Pair<Pokemon, Int>> = mutableListOf()

        getPotentialPokemon(superEffectiveTypes, targetPokemon).sortedByDescending{ it.second }.forEach { pair ->
            potentialPokemon.add(pair)
            counters.add(pair.first)
        }
        getPotentialPokemon(effectiveTypes, targetPokemon).sortedByDescending{ it.second }.forEach { pair ->
            potentialPokemon.add(pair)
            if(!counters.contains(pair.first)) {
                counters.add(pair.first)
            }
        }
        return counters
    }

    private fun getPotentialPokemon(types: MutableList<Int>, targetPokemon: Pokemon): MutableList<Pair<Pokemon, Int>> {
        val potentialPokemon: MutableList<Pair<Pokemon, Int>> = mutableListOf()
        types.forEach { type ->
            val pokemonList = pokemonRepository.getPokemonWithType(type)
            pokemonList.forEach { pokemon ->
                val physicalDiff = pokemon.stats.attack - targetPokemon.stats.defense
                val specialDiff = pokemon.stats.specialAttack - targetPokemon.stats.specialDefense
                if(max(physicalDiff, specialDiff) > 10 &&
                    !pokemon.name.contains("-gmax") &&
                    !pokemon.name.contains("-mega") &&
                    !pokemon.name.contains("-primal") &&
                    !pokemon.name.contains("-ultra")) {
                    potentialPokemon.add(Pair(pokemon, max(physicalDiff, specialDiff)))
                }
            }
        }
        return potentialPokemon
    }
}