import requests
import json

pokedex = {}
i = 1
response = requests.get(f"https://pokeapi.co/api/v2/type")
response = response.json()
types = {}
for index, type in enumerate(response['results']):
    types[index + 1] = type['name']
with open("types.json", "w") as file:
    json.dump(types, file)

#with open("types.json", "w") as file:
#    json.dump(pokedex, file)