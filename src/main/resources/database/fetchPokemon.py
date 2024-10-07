import requests
import json

def fetchPokemon():
    print("fetching pokemon")
    pokedex = {}
    pokemonPageResponse = requests.get(f"https://pokeapi.co/api/v2/pokemon")

    while True:
        pokemonPage = pokemonPageResponse.json()
        for pokemon in pokemonPage['results']:
            pokemonData = requests.get(f"{pokemon['url']}").json()
            print(f"{pokemonData['id']} - {pokemonData['name']}")
            pokedex[f"{pokemonData['id']}"] = pokemonData

        if pokemonPage['next'] != None:
            pokemonPageResponse = requests.get(f"{pokemonPage['next']}")
        else:
            break

    with open("pokemon.json", "w") as file:
        json.dump(pokedex, file)