export interface PokemonData {
    sprites: Sprites,
    types: object,
}

export interface ApiTypes {
    slot: number,
    type: Type,
}

export interface PokemonTypes {
    [key: string]: string 
}

interface Type {
    name: string,
    url: string,
}

interface Sprites {
    back_default: string,
    front_default: string,
    front_shiny: string,
    back_shiny: string,
}