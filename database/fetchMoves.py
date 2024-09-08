import requests
import json

pokedex = {}
url = "https://pokeapi.co/api/v2/move/"
i = 1
response = requests.get(url + str(i))

while True:
    if response.status_code == 200:
        pokemon = response.json()
        pokedex[str(i)] = pokemon
        print(i)
        i += 1
        response = requests.get(url + str(i))
    else:
        break

with open("moves.json", "w") as file:
    json.dump(pokedex, file)
