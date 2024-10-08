import psycopg
import os
import json
from fetchTypes import fetchTypes
from fetchPokemon import fetchPokemon
from fetchMoves import fetchMoves



def writeTypesToDatabase(cur):
    with open("types.json") as file:
        data = json.load(file)
        for id in data.keys():
            cur.execute(
                """
                INSERT INTO types(id, name) 
                VALUES(%s, %s)
                """,
                (id, data[id]['name'])
            )

def writeLearnedByPokemon(cur):
    with open("moves.json", "r") as file:
        moveData = json.load(file)
        for moveID in moveData:
            for pokemon in moveData[moveID]["learned_by_pokemon"]:
                pokemonID = pokemon["url"].split("/")[-2]
                cur.execute(
                    """
                    INSERT INTO pokemonavailablemoves(moveid, pokemonid)
                    VALUES(%s, %s)
                    """,
                    (moveID, pokemonID)
                )


def writePokemonToDatabase(cur):
    with open("pokemon.json") as file:
        data = json.load(file)
        relavantData = {}
        for pokedexID in data.keys():
            relavantData['name'] = data[pokedexID]['name']
            relavantData['id'] = data[pokedexID]['id']
            relavantData['sprite_front'] = data[pokedexID]["sprites"]['front_default']
            relavantData['sprite_back'] = data[pokedexID]['sprites']['back_default']
            for stat in data[pokedexID]['stats']:
                relavantData[stat['stat']['name']] = stat['base_stat'] 
            for typeData in data[pokedexID]['types']:
                typeID = typeData["type"]["url"].split("/")[-2]
                relavantData[f'type{typeData["slot"]}'] = typeID

            if not "type2" in relavantData:
                cur.execute(
                    """
                    INSERT INTO pokemon(name, pokedexID, type1, hp, attack,
                            defense, special_attack, special_defense,
                            speed, sprite_front, sprite_back)
                    VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
                    """,
                (relavantData['name'], relavantData['id'], 
                relavantData['type1'], relavantData['hp'],
                relavantData['attack'], relavantData['defense'], 
                relavantData['special-attack'], relavantData['special-defense'], 
                relavantData['speed'], relavantData['sprite_front'],
                relavantData['sprite_back']))                   
            else:
                cur.execute(
                    """
                    INSERT INTO pokemon(name, pokedexID, type1, type2, hp, attack,
                            defense, special_attack, special_defense,
                            speed, sprite_front, sprite_back)
                    VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
                    """,
                (relavantData['name'], relavantData['id'], 
                relavantData['type1'], relavantData['type2'],
                relavantData['hp'], relavantData['attack'], 
                relavantData['defense'], relavantData['special-attack'], 
                relavantData['special-defense'], relavantData['speed'],
                relavantData['sprite_front'], relavantData['sprite_back']))

            relavantData.clear()


def writeToDamageRelations(cur):
    with open("damageRelations.json", "r") as file: 
        data = json.load(file)
        for typePair in data:
            types = typePair.split(" ")
            cur.execute(
                """
                INSERT INTO damagerelations(type1, type2, damagerelation)
                VALUES(%s, %s, %s)
                """,
                (int(types[0]), int(types[1]), data[typePair])
            )

def writeMoves(cur):
    with open("moves.json", "r") as file: 
        moveData = json.load(file)
        for moveID in moveData: 
            move = moveData[moveID]
            typeID = move['type']['url'].split("/")[-2]
            cur.execute(
                """
                INSERT INTO moves(moveid, name, power, accuracy, type, priority)
                VALUES(%s, %s, %s, %s, %s, %s)
                """,
                (moveID, move['name'], move['power'], move['accuracy'], typeID, move['priority'])
            )


with psycopg.connect("dbname=pokemon user=backend password=123qwe") as connection:

    if(not os.path.isfile("./types.json")): fetchTypes()
    if(not os.path.isfile("./pokemon.json")): fetchPokemon()
    if(not os.path.isfile("./moves.json")): fetchMoves()
    #if(not os.path.isfile("./damageRelations.json")): fetchMoves()

    with connection.cursor() as cur:
        writeTypesToDatabase(cur)
        writePokemonToDatabase(cur)
        writeToDamageRelations(cur)
        writeMoves(cur)
        writeLearnedByPokemon(cur)
    

