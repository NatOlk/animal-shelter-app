import React from "react";
import { useMutation } from "@apollo/client";
import { VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY, DELETE_VACCINATION } from '../common/graphqlQueries.js';
import { Button } from "@nextui-org/react";

function DeleteVaccination({ id }) {
    const [deleteVaccination] = useMutation(DELETE_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY]
    });

    return (
        <Button onClick={() => deleteVaccination({ variables: { id: id } })}>
            <i className="material-icons">close</i>
        </Button>
    )
}

export default DeleteVaccination;