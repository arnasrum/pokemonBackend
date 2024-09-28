package com.arnas.pokemans.repository

import com.arnas.pokemans.dataclasses.Pokemon
import com.arnas.pokemans.dataclasses.PokemonSprites
import com.arnas.pokemans.dataclasses.PokemonStats
import com.arnas.pokemans.dataclasses.PokemonType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterUtils
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource


@Repository
class PokemonRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun getAllPokemon(): MutableList<Pokemon> {
        val pokemonList: MutableList<Pokemon> = mutableListOf()
        val sql: String = "" +
                "SELECT p.pokedexid, p.name, types1.name AS type1, types2.name AS type2, " +
                "p.hp, p.attack, p.special_attack, p.defense, p.special_defense, p.speed, " +
                "p.sprite_front, p.sprite_back " +
                "FROM pokemon AS p INNER JOIN types AS types1 ON p.type1=types1.id " +
                "LEFT JOIN types AS types2 ON p.type2=types2.id"

        val namedjdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        namedjdbcTemplate.query(sql, RowMapper {resultSet, _ ->
            while (resultSet.next()) {
                pokemonList.add(extractPokemonFromResultSet(resultSet))
            }
        })
        return pokemonList
    }

    fun getPokemonByName(name: String): Pokemon? {
        //val sql: String = "SELECT * FROM pokemon WHERE name=?"
        val sql: String = "" +
                "SELECT p.pokedexid, p.name, types1.name AS type1, types2.name AS type2, " +
                "p.hp, p.attack, p.special_attack, p.defense, p.special_defense, p.speed, " +
                "p.sprite_front, p.sprite_back " +
                "FROM pokemon AS p INNER JOIN types AS types1 ON p.type1=types1.id " +
                "LEFT JOIN types AS types2 ON p.type2=types2.id " +
                "WHERE p.name=:name"

        val parameters = MapSqlParameterSource()
        parameters.addValue("name", name)
        val namedjdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        val resultPokemon: MutableList<Pokemon> = namedjdbcTemplate.query(sql, parameters, RowMapper {resultSet, _ ->
            extractPokemonFromResultSet(resultSet)
        })
        return resultPokemon.getOrNull(0)
    }

    private fun extractPokemonFromResultSet(resultSet: ResultSet): Pokemon {

        val type = PokemonType(
            resultSet.getString("type1"),
            resultSet.getString("type2")
        )

        val sprites = PokemonSprites(resultSet.getString("sprite_front"),
            resultSet.getString("sprite_back")
        )
        val stats = PokemonStats(
            hp = resultSet.getInt("hp"),
            attack = resultSet.getInt("attack"),
            defense = resultSet.getInt("defense"),
            specialAttack = resultSet.getInt("special_attack"),
            specialDefense = resultSet.getInt("special_defense"),
            speed = resultSet.getInt("speed")
        )
        val resultPokemon = Pokemon(
            name = resultSet.getString("name"),
            pokedexID = resultSet.getInt("pokedexID"),
            type,
            stats,
            sprites
        )
        return resultPokemon
    }
}