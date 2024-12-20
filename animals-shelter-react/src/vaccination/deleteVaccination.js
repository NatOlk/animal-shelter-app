import React from "react";
import { useMutation } from "@apollo/client";
import { VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY, DELETE_VACCINATION } from '../common/graphqlQueries.js';

function DeleteVaccination({ id }) {
    const [deleteVaccination] = useMutation(DELETE_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY]
    });

    return (
        <button className="waves-effect waves-light btn-small" onClick={() =>
            deleteVaccination({ variables: { id: id } })}>
            <i className="material-icons">close</i>
        </button>
    )
}

export default DeleteVaccination;