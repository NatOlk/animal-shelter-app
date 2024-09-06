import React from "react";
import {gql} from "graphql-tag";
import {useMutation} from "@apollo/client";
import { ANIMALS_QUERY } from '../graphqlQueries.js';

const DELETE_ANIMAL = gql`
    mutation ($name: String!, $species: String!)
    {
        deleteAnimal(name: $name, species: $species)
        {
            name
            species
        }
    }
`;

function DeleteAnimal ({name, species})
{
    const [deleteAnimal, { data2 }] = useMutation(DELETE_ANIMAL, {
        refetchQueries: [ANIMALS_QUERY]
    });

   return (
       <button onClick={() =>
           deleteAnimal({variables: {name: name, species: species}})} className="round-button-with-border">
                 Delete
       </button>
   )
}

export default DeleteAnimal;