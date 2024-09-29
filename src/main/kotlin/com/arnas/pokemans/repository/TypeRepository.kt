package com.arnas.pokemans.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class TypeRepository {


    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun getCounterTypes(type: Int): MutableList<Pair<Int, Float>> {

        val counterTypes = mutableListOf<Pair<Int, Float>>()
        val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        val sql: String = "" +
                "SELECT type1, damagerelation " +
                "FROM damagerelations " +
                "WHERE type2=:type"

        val parameters = MapSqlParameterSource()
        parameters.addValue("type", type)

        namedParameterJdbcTemplate.queryForObject(sql, parameters, { resultSet, _ ->
            do {
                counterTypes.add(
                    Pair(resultSet.getInt("type1"),
                        resultSet.getFloat("damagerelation"))
                )
            } while(resultSet.next())
        })
        return counterTypes
    }
}