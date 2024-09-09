import json

with open("pokemon.json") as file:
    pokedex = dict(json.load(file))
    print(pokedex["\"2\""])

