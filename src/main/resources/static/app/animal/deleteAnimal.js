import React from "react";
import {gql} from "graphql-tag";
import {useMutation} from "@apollo/client";
import { ANIMALS_QUERY } from '../graphqlQueries.js';

const DELETE_ANIMAL = gql`
    mutation ($id: ID!)
    {
        deleteAnimal(id: $id)
        {
            id
        }
    }
`;

function DeleteAnimal ({id})
{
   const [deleteAnimal, { loading, error }] = useMutation(DELETE_ANIMAL, {
       update(cache, { data: { deleteAnimal } }) {
           const existingAnimals = cache.readQuery({ query: ANIMALS_QUERY });
           const newAnimals = existingAnimals.allAnimals.filter(animal => animal.id !== deleteAnimal.id);
           cache.writeQuery({
               query: ANIMALS_QUERY,
               data: { allAnimals: newAnimals },
           });
       }
   });

    if (loading) return <p>Deleting...</p>;
    if (error) return <p>Error: {error.message}</p>;

   return (
       <button onClick={() =>
           deleteAnimal({variables: {id: id}})} className="round-button-with-border">
                 Delete
       </button>
   )
}

export default DeleteAnimal;