import { useState, Dispatch} from "react";

export default function PokemonForm(props: {
    setQuery: any,
}) {

    const [pokemonName, setPokemonName] = useState("");

    return(
        <>
            <form onSubmit={(event) => {
                    event.preventDefault();
                    props.setQuery(pokemonName);
                }}>
                <input placeholder="Input Pokemon Name" onChange={(event) => setPokemonName(event.target.value)}></input> 
                <button type="submit">Submit</button>
            </form>
        </>
    );
}