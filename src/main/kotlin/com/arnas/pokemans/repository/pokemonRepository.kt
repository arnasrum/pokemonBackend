package com.arnas.pokemans.repository

import com.arnas.pokemans.dataclasses.Pokemon
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource


@Repository
class PokemonRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    @Autowired
    lateinit var con: DataSource

    fun getAllPokemon(): MutableList<Pokemon> {
        val pokemonList: MutableList<Pokemon> = mutableListOf()
        //val sql: String = "SELECT * FROM pokemon WHERE name=?"
        val sql: String = "SELECT * FROM pokemon"
        val preparedStatement: PreparedStatement = con.connection.prepareStatement(sql)
        //preparedStatement.setString(1, name)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        while(resultSet.next())  {
            pokemonList.add(
                Pokemon(
                    name = resultSet.getString("name"),
                    pokedexID = resultSet.getInt("pokedexID"),
                    type1 = resultSet.getInt("type1"),
                    type2 = resultSet.getInt("type2"),
                    hp = resultSet.getInt("hp"),
                    attack = resultSet.getInt("attack"),
                    defense = resultSet.getInt("defense"),
                    specialAttack = resultSet.getInt("special_attack"),
                    specialDefense = resultSet.getInt("special_defense"),
                    speed = resultSet.getInt("speed"),
                    spriteFront = resultSet.getString("sprite_front"),
                    spriteBack = resultSet.getString("sprite_back")
                )
            )
        }
        return pokemonList
    }

    fun getPokemonByName(name: String): Pokemon? {
        val sql: String = "SELECT * FROM pokemon WHERE name=?"
        val preparedStatement: PreparedStatement = con.connection.prepareStatement(sql)
        preparedStatement.setString(1, name)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        var resultPokemon: Pokemon? = null

        if (resultSet.next())  {
            resultPokemon = Pokemon(
                name = resultSet.getString("name"),
                pokedexID = resultSet.getInt("pokedexID"),
                type1 = resultSet.getInt("type1"),
                type2 = resultSet.getInt("type2"),
                hp = resultSet.getInt("hp"),
                attack = resultSet.getInt("attack"),
                defense = resultSet.getInt("defense"),
                specialAttack = resultSet.getInt("special_attack"),
                specialDefense = resultSet.getInt("special_defense"),
                speed = resultSet.getInt("speed"),
                spriteFront = resultSet.getString("sprite_front"),
                spriteBack = resultSet.getString("sprite_back")
            )
        }
        return resultPokemon
    }
}