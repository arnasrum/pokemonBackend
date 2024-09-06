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
	accuracy INT
	type TEXT,	
	PRIMARY KEY(moveID)
);

CREATE TABLE IF NOT EXISTS pokemonAvailableMoves(
	moveID INT,
	pokemonID INT,
	PRIMARY KEY(moveID, pokemonID),
	FOREIGN KEY(moveID) REFERENCES moves(moveID),
	FOREIGN KEY(pokemonID) REFERENCES moves(pokemonID),
);
