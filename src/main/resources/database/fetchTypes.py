import requests
import json

i = 1
response = requests.get(f"https://pokeapi.co/api/v2/type/{i}")
types = {}
damageRelations = {}
while True:
    if response.status_code == 200:
        typeData = {}
        #relation = {}
        data = response.json()
        typeData['id'] = data['id']
        typeData['name'] = data['name']
        typeData['damage_class'] = data['move_damage_class']
        for relation in data['damage_relations']:
            #print(data['damage_relations'][relation])
            if relation == "double_damage_to":
                for type in data['damage_relations'][relation]:
                    id = type['url'].split("/")[-2]
                    damageRelations[f'{typeData['id']} {id}'] = 2.0
            if relation == "half_damage_to":
                for type in data['damage_relations'][relation]:
                    id = type['url'].split("/")[-2]
                    damageRelations[f'{typeData['id']} {id}'] = 0.5 
            if relation == "no_damage_to":
                for type in data['damage_relations'][relation]:
                    id = type['url'].split("/")[-2]
                    damageRelations[f'{typeData['id']} {id}'] = 0.0
        types[i] = typeData
        i += 1
        response = requests.get(f"https://pokeapi.co/api/v2/type/{i}")
    else:
        break

with open("types.json", "w") as file:
    json.dump(types, file)

with open("damageRelations.json", "w") as file:
    json.dump(damageRelations, file)