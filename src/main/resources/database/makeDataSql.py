import psycopg
import os
import json
import re
from fetchTypes import fetchTypes
from fetchPokemon import fetchPokemon
from fetchMoves import fetchMoves


def makeInsertStatement(header, dataTuples):
    dataTuples[-1] = dataTuples[-1] + ";\n"
    pattern = re.compile(r"None")
    for i, tuple in enumerate(dataTuples):
        tuple = tuple.replace("None", "null")
        dataTuples[i] = tuple
    return f"{header}\n" + ",\n".join(dataTuples)


def makeTypeInsertStatement():
    with open("types.json") as file:
        data = json.load(file)
        sql = "INSERT INTO types(id, name) VALUES"
        dataTuples = []
        for id in data.keys():
            dataTuples.append(f"({int(id)}, '{str(data[id]['name'])}')")
        return makeInsertStatement(sql, dataTuples)

def makeMoveLearnedInsertStatement():
    with open("moves.json", "r") as file:
        moveData = json.load(file)

        sql = "INSERT INTO pokemonavailablemoves(moveid, pokemonid) VALUES"
        dataTuples = []

        for moveID in moveData:
            for pokemon in moveData[moveID]["learned_by_pokemon"]:
                pokemonID = pokemon["url"].split("/")[-2]
                dataTuples.append(f"({int(moveID)}, {int(pokemonID)})")
        return makeInsertStatement(sql, dataTuples)


def makePokemonInsertStatement():
    with open("pokemon.json") as file:
        data = json.load(file)
        relevantData = {}

        insertWithBothTypes = f"INSERT INTO pokemon(name, pokedexID, type1, type2, hp, attack, defense, special_attack, special_defense, speed, sprite_front, sprite_back) VALUES"
        insertWithOneType = f"INSERT INTO pokemon(name, pokedexID, type1, hp, attack, defense, special_attack, special_defense, speed, sprite_front, sprite_back) VALUES"
        oneTypeData = []
        twoTypeData = []


        for pokedexID in data.keys():
            relevantData.clear()
            relevantData['name'] = f"'{data[pokedexID]['name']}'"
            relevantData['id'] = data[pokedexID]['id']
            relevantData['sprite_front'] = data[pokedexID]["sprites"]['front_default']
            relevantData['sprite_back'] = data[pokedexID]['sprites']['back_default']
            if relevantData["sprite_front"]:
                relevantData["sprite_front"] = f"'{relevantData['sprite_front']}'"
            if relevantData["sprite_back"]:
                relevantData["sprite_back"] = f"'{relevantData['sprite_back']}'"

            for stat in data[pokedexID]['stats']:
                relevantData[stat['stat']['name']] = stat['base_stat'] 
            for typeData in data[pokedexID]['types']:
                typeID = typeData["type"]["url"].split("/")[-2]
                relevantData[f'type{typeData["slot"]}'] = typeID

            dataTuple = [str(relevantData['name']), int(relevantData['id']), 
                int(relevantData['type1']), int(relevantData['hp']), int(relevantData['attack']), 
                int(relevantData['defense']), int(relevantData['special-attack']), 
                int(relevantData['special-defense']), int(relevantData['speed']),
                str(relevantData['sprite_front']), str(relevantData['sprite_back'])]

            if "type2" in relevantData:
                dataTuple.insert(3, int(relevantData["type2"]))
                twoTypeData.append("(" + ", ".join([str(element) for element in dataTuple]) + ")")
                continue
            oneTypeData.append("(" + ", ".join([str(element) for element in dataTuple]) + ")")
        insertStatement1 = makeInsertStatement(insertWithOneType, oneTypeData)
        insertStatement2 = makeInsertStatement(insertWithBothTypes, twoTypeData)
        return insertStatement1, insertStatement2


def makeDamageRelationInsertStatement():
    with open("damageRelations.json", "r") as file: 
        data = json.load(file)
        header = "INSERT INTO damagerelations(type1, type2, damagerelation) VALUES"
        dataTuples = []
        for typePair in data:
            types = typePair.split(" ")
            dataTuples.append(f"({int(types[0])}, {int(types[1])}, {data[typePair]})")
        return makeInsertStatement(header, dataTuples)

def makeMoveInsertStatement():
    with open("moves.json", "r") as file: 
        moveData = json.load(file)
        header = "INSERT INTO moves(moveid, name, power, accuracy, type, priority) VALUES"
        dataTuples = []
        for moveID in moveData: 
            move = moveData[moveID]
            typeID = move['type']['url'].split("/")[-2]
            dataTuples.append(f"({int(moveID)}, '{str(move['name'])}', {move['power']}, {move['accuracy']}, {int(typeID)}, {int(move['priority'])})")
        return makeInsertStatement(header, dataTuples)



if __name__ == "__main__":
    if(not os.path.isfile("./types.json")): fetchTypes()
    if(not os.path.isfile("./pokemon.json")): fetchPokemon()
    if(not os.path.isfile("./moves.json")): fetchMoves()
    with open("data.sql", "w") as file:
        file.write("\n".join([makeTypeInsertStatement(), *makePokemonInsertStatement(), makeMoveInsertStatement(), makeMoveLearnedInsertStatement(), makeDamageRelationInsertStatement()]))
