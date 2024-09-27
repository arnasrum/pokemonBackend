package com.arnas.pokemans.repository

import com.arnas.pokemans.dataclasses.Pokemon
import com.arnas.pokemans.dataclasses.PokemonSprites
import com.arnas.pokemans.dataclasses.PokemonStats
import com.arnas.pokemans.dataclasses.PokemonType
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
        val sql: String = "SELECT * FROM pokemon"
        val preparedStatement: PreparedStatement = con.connection.prepareStatement(sql)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        while(true) {
            val pokemon: Pokemon = extractPokemonFromResultSet(resultSet) ?: break
            pokemonList.add(pokemon)
        }
        return pokemonList
    }

    fun getPokemonByName(name: String): Pokemon? {
        val sql: String = "SELECT * FROM pokemon WHERE name=?"
        val preparedStatement: PreparedStatement = con.connection.prepareStatement(sql)
        preparedStatement.setString(1, name)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        val resultPokemon: Pokemon? = extractPokemonFromResultSet(resultSet)

        return resultPokemon
    }

    private fun getTypeName(type1: Int, type2: Int?): Array<String> {
        var sql: String = "SELECT * FROM types WHERE id=?"
        if(type2 != null) sql = "$sql OR id=?"
        val preparedStatement: PreparedStatement = con.connection.prepareStatement(sql)
        preparedStatement.setInt(1, type1)
        if(type2 != null) preparedStatement.setInt(2, type2)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        resultSet.next()
        val type1Name = resultSet.getString("name")
        if(type2 != null) {
            resultSet.next()
            val type2Name = resultSet.getString("name")
            return arrayOf(type1Name, type2Name)
        }
        return arrayOf(type1Name)
    }

    private fun extractPokemonFromResultSet(resultSet: ResultSet): Pokemon? {
        if(resultSet.next()) {

            var type2: Int? = resultSet.getInt("type2")
            if (type2 == 0) type2 = null
            val typeNames = getTypeName(
                resultSet.getInt("type1"),
                type2
            )
            val type = PokemonType(
                typeNames[0],
                typeNames.elementAtOrNull(1)
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
        return null
    }


}