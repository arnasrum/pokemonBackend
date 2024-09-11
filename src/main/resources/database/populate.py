import psycopg
import json



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
    with open("learnedByPokemon.json", "r") as file:
        moveData = json.load(file)
        for moveID in moveData:
            for pokemon in moveData[moveID]:
                cur.execute(
                    """
                    INSERT INTO pokemonavailablemoves(moveid, pokemonid, pokemonname)
                    VALUES(%s, %s, %s)
                    """,
                    (moveID, pokemon['id'], pokemon['name'])
                )


def writePokemonToDatabase(cur):
    with open("pokemon.json") as file:
        data = json.load(file)
        relavantData = {}
        for pokedexID in data.keys():
            relavantData['name'] = data[pokedexID]['name']
            relavantData['id'] = data[pokedexID]['id']
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
                            speed)
                    VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s);
                    """,
                (relavantData['name'], relavantData['id'], 
                relavantData['type1'], relavantData['hp'],
                relavantData['attack'], relavantData['defense'], 
                relavantData['special-attack'], relavantData['special-defense'], 
                relavantData['speed']))                   
            else:
                cur.execute(
                    """
                    INSERT INTO pokemon(name, pokedexID, type1, type2, hp, attack,
                            defense, special_attack, special_defense,
                            speed)
                    VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
                    """,
                (relavantData['name'], relavantData['id'], 
                relavantData['type1'], relavantData['type2'],
                relavantData['hp'], relavantData['attack'], 
                relavantData['defense'], relavantData['special-attack'], 
                relavantData['special-defense'], relavantData['speed']))

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
    with connection.cursor() as cur:
        writeTypesToDatabase(cur)
        writePokemonToDatabase(cur)
        writeToDamageRelations(cur)
        writeMoves(cur)
        writeLearnedByPokemon(cur)
    

