DROP TABLE IF EXISTS pokemon, moves, pokemonAvailableMoves, types, damageRelations;

CREATE TABLE IF NOT EXISTS types(
    id INT,
    name TEXT,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS pokemon(
	name TEXT,
	pokedexID INT, 
	type1 INT NOT NULL,
	type2 INT,
	hp INT, attack INT,
	defense INT, special_attack INT,
    special_defense INT, speed INT,
	PRIMARY KEY(name, pokedexID),
	FOREIGN KEY(type1) REFERENCES types(id),
	FOREIGN KEY(type2) REFERENCES types(id)
);

CREATE TABLE IF NOT EXISTS moves(
	moveID INT,
	name TEXT,
	power INT,
	accuracy INT,
	type INT,	
	priority INT,
	PRIMARY KEY(moveID),
	FOREIGN KEY(type) REFERENCES types(id)
);

CREATE TABLE IF NOT EXISTS pokemonAvailableMoves(
	moveID INT,
	pokemonID INT,
	pokemonName TEXT,
	PRIMARY KEY(moveID, pokemonID),
	FOREIGN KEY(moveID) REFERENCES moves(moveID),
	FOREIGN KEY(pokemonID, pokemonName) REFERENCES pokemon(pokedexID, name)
);


CREATE TABLE IF NOT EXISTS damageRelations(
    type1 INT,
    type2 INT,
    damageRelation FLOAT,
    PRIMARY KEY(type1, type2),
    FOREIGN KEY(type1) REFERENCES types(id),
    FOREIGN KEY(type2) REFERENCES types(id)
);