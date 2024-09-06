import React from "react";
import {gql} from "graphql-tag";
import {useMutation} from "@apollo/client";
import { VACCINATIONS_QUERY } from '../graphqlQueries.js';

const DELETE_VACCINATION = gql`
    mutation ($name: String!, $species: String!, $vaccine: String!, $batch: String!)
    {
        deleteVaccination(name: $name, species: $species, vaccine: $vaccine, batch: $batch)
        {
            name
            species
            vaccine
            batch
        }
    }
`;

function DeleteVaccination ({vaccination})
{
    const [deleteVaccination, { data2 }] = useMutation(DELETE_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY]
    });

   return (
       <button onClick={() =>
           deleteVaccination({variables: {name: vaccination.name, species: vaccination.species,
           vaccine: vaccination.vaccine, batch: vaccination.batch}})} className="round-button-with-border">
                 Delete
       </button>
   )
}

export default DeleteVaccination;