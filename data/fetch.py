import requests
import json

pokedex = {}
i = 1
response = requests.get(f"https://pokeapi.co/api/v2/pokemon/{i}")

while True:
    if response.status_code == 200:
        pokemon = response.json()
        pokedex[f"\"{i}\""] = pokemon
        print(i)
        i += 1
        response = requests.get(f"https://pokeapi.co/api/v2/pokemon/{i}")
    else:
        break

with open("pokemon.json", "w") as file:
    json.dump(pokedex, file)