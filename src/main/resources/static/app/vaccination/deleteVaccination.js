import React from "react";
import {gql} from "graphql-tag";
import {useMutation} from "@apollo/client";
import { VACCINATIONS_QUERY } from '../graphqlQueries.js';

const DELETE_VACCINATION = gql`
    mutation ($id: ID!)
    {
        deleteVaccination(id: $id)
        {
            id
            vaccine
            batch
        }
    }
`;

function DeleteVaccination ({id})
{
    const [deleteVaccination, { data2 }] = useMutation(DELETE_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY]
    });

   return (
       <button onClick={() =>
           deleteVaccination({variables: {id: id}})} className="round-button-with-border">
                 Delete
       </button>
   )
}

export default DeleteVaccination;