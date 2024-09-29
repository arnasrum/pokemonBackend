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
import java.sql.SQLException
import javax.sql.DataSource


@Repository
class PokemonRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun getAllPokemon(): MutableList<Pokemon> {
        val pokemonList: MutableList<Pokemon> = mutableListOf()
        val sql: String = "" +
                "SELECT p.pokedexid, p.name, types1.name AS type1name, types2.name AS type2name, " +
                "types1.id AS type1, types2.id AS type2, " +
                "p.hp, p.attack, p.special_attack, p.defense, p.special_defense, p.speed, " +
                "p.sprite_front, p.sprite_back " +
                "FROM pokemon AS p INNER JOIN types AS types1 ON p.type1=types1.id " +
                "LEFT JOIN types AS types2 ON p.type2=types2.id"

        val namedjdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        namedjdbcTemplate.query(sql, RowMapper {resultSet, _ ->
            pokemonList.add(extractPokemonFromResultSet(resultSet))
            while (resultSet.next()) {
                pokemonList.add(extractPokemonFromResultSet(resultSet))
            }
        })
        return pokemonList
    }

    fun getPokemonByName(name: String): Pokemon? {
        //val sql: String = "SELECT * FROM pokemon WHERE name=?"
        val sql: String = "" +
                "SELECT p.pokedexid, p.name, types1.name AS type1name, types2.name AS type2name, " +
                "types1.id AS type1, types2.id AS type2, " +
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


    fun getPokemonWithType(type: Int): MutableList<Pokemon> {
        val pokemon: MutableList<Pokemon> = mutableListOf()
        val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        val mapSqlParameterSource = MapSqlParameterSource()

        var sql = "SELECT p.pokedexid, p.name, types1.name AS type1name, types2.name AS type2name, " +
                "types1.id AS type1, types2.id AS type2, " +
                "p.hp, p.attack, p.special_attack, p.defense, p.special_defense, p.speed, " +
                "p.sprite_front, p.sprite_back " +
                "FROM pokemon AS p INNER JOIN types AS types1 ON p.type1=types1.id " +
                "LEFT JOIN types AS types2 ON p.type2=types2.id " +
                "WHERE type1=:type OR type2=:type"

        mapSqlParameterSource.addValue("type", type)
        namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, RowMapper {resultSet, _ ->
            do {
                pokemon.add(extractPokemonFromResultSet(resultSet))
            } while (resultSet.next())
        })
        return pokemon
    }

    fun extractPokemonFromResultSet(resultSet: ResultSet): Pokemon {

        var type: PokemonType? = null
        try {
            type = PokemonType(
                resultSet.getString("type1name"),
                resultSet.getString("type2name")
            )
        } catch (e: SQLException) {}
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

        var type2: Int? = resultSet.getInt("type2")
        if(type2 == 0)  type2 = null
        val resultPokemon = Pokemon(
            name = resultSet.getString("name"),
            pokedexID = resultSet.getInt("pokedexID"),
            type,
            stats,
            sprites,
            type1 = resultSet.getInt("type1"),
            type2 = type2

        )
        return resultPokemon
    }


}