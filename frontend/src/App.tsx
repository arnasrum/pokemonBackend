import './App.css';
import './style.css';
import { useState, useEffect } from 'react';
import PokemonForm from './components/PokemonForm';
import {PokemonData, ApiTypes} from "./components/Types";
import { Types } from './components/PokemonTypes';

function App() {

    const [query, setQuery] = useState<String>();
    const [pokemonData, setPokemonData] = useState<PokemonData>();

    useEffect(() => {
        if(query) {
            let url: string = "https://pokeapi.co/api/v2/pokemon/";
            url += query;
            console.log(url);
            fetch(url)
                .then(response => response.json())
                //.then(data => console.log(data))
                .then(data => setPokemonData(data))
                .catch(error => {
                    console.log(error);
                    setQuery(undefined);
                });
            }
    }, [query]);

    if(query && pokemonData) {
        return (
        <>
            <div>
                {query}
            </div>
            <div>
                <img src={pokemonData.sprites.front_default} />
                <img src={pokemonData.sprites.back_default} />
            </div>
            <div>
                <img src={pokemonData.sprites.front_shiny} />
                <img src={pokemonData.sprites.back_shiny} />
            </div>
            <div>
                {Object.values(pokemonData.types).map( (item: ApiTypes, index: number) => {
                    console.log(item.type.name, index);
                    return (<img className={item.type.name} 
                        key={index} 
                        src={Types[item.type.name]} 
                        width={30} 
                        height={30}
                    />);
                })}
            </div>
            <PokemonForm setQuery={setQuery}/>
        </>
    
    );
    } else {
        return (
            <>
                <PokemonForm setQuery={setQuery}/>
            </>
        );
    }
}

export default App
