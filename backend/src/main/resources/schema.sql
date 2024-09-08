CREATE TABLE IF NOT EXISTS pokemon(
	name TEXT,
	pokedexID INT, 
	type1 TEXT,
	type2 TEXT,
	PRIMARY KEY(name, pokedexID)
);

CREATE TABLE IF NOT EXISTS moves(
	name TEXT,
	moveID INT,
	power INT,
	accuracy INT,
	type TEXT,	
	PRIMARY KEY(moveID)
);

CREATE TABLE IF NOT EXISTS pokemonAvailableMoves(
	moveID INT,
	pokemonID INT,
	pokemonName TEXT,
	PRIMARY KEY(moveID, pokemonID),
	FOREIGN KEY(moveID) REFERENCES moves(moveID),
	FOREIGN KEY(pokemonID, pokemonName) REFERENCES pokemon(pokedexID, name)
);

CREATE TABLE IF NOT EXISTS type(
    name TEXT,
    id int,
    PRIMARY KEY(id)
);