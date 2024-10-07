import requests
import json

def fetchMoves():
    print("fetching moves")

    movedex = {}
    movePage = requests.get("https://pokeapi.co/api/v2/move/")

    while True:
        movePageData = movePage.json()        
        for move in movePageData['results']:
            moveData = requests.get(move['url']).json()
            movedex[f"{moveData['id']}"] = moveData
            print(f"{moveData['id']} - {moveData['name']}")
        if movePageData['next'] != None:
            movePage = requests.get(movePageData['next'])
        else:
            break

    with open("moves.json", "w") as file:
        json.dump(movedex, file)