import requests
import json

typePageRequest = requests.get(f"https://pokeapi.co/api/v2/type/")
types = {}
damageRelations = {}
while True:
    typePage = typePageRequest.json()

    for type in typePage['results']:
        typeData = requests.get(type['url']).json()
        types[f"{typeData['id']}"] = typeData
        for relation in typeData['damage_relations']:
            #print(data['damage_relations'][relation])
            if relation == "double_damage_to":
                for type in typeData['damage_relations'][relation]:
                    id = type['url'].split("/")[-2]
                    damageRelations[f'{typeData['id']} {id}'] = 2.0
            if relation == "half_damage_to":
                for type in typeData['damage_relations'][relation]:
                    id = type['url'].split("/")[-2]
                    damageRelations[f'{typeData['id']} {id}'] = 0.5 
            if relation == "no_damage_to":
                for type in typeData['damage_relations'][relation]:
                    id = type['url'].split("/")[-2]
                    damageRelations[f'{typeData['id']} {id}'] = 0.0
    if typePage['next'] != None:
        typePageRequest = requests.get(typePage['next'])
    else:
        break

with open("types.json", "w") as file:
    json.dump(types, file)

with open("damageRelations.json", "w") as file:
    json.dump(damageRelations, file)