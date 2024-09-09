import psycopg
import json


with psycopg.connect("dbname=pokemon user=backend password=123qwe") as connection:
    with connection.cursor() as cur:

        with open("types.json") as file:
            data = json.load(file)
            for id in data.keys():
                cur.execute(
                    """
                    INSERT INTO types(id, name) 
                    VALUES(%s, %s)
                    """,
                    (id, data[id])
                )

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
                    relavantData[f'type{typeData['slot']}'] = typeID

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