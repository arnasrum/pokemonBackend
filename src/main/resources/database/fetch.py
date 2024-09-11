import requests
import json



def fetchPage(url):
    movedex = {}
    movePage = requests.get("https://pokeapi.co/api/v2/move/")

    while True:
        movePageData = movePage.json()        
        for move in movePageData['results']:
            moveData = requests.get(move['url']).json()
            print(moveData['id'])
            movedex[f"{moveData['id']}"] = moveData
        if movePageData['next'] != None:
            movePage = requests.get(movePageData['next'])
        else:
            break

    with open("moves.json", "w") as file:
        json.dump(movedex, file)