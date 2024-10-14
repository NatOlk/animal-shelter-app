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
       <button className="waves-effect waves-light btn-small" onClick={() =>
           deleteVaccination({variables: {id: id}})}>
                 Delete
       </button>
   )
}

export default DeleteVaccination;