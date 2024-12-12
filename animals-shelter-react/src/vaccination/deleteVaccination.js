import React from "react";
import { useMutation } from "@apollo/client";
import { Button } from "@nextui-org/react";
import { VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY, DELETE_VACCINATION } from '../common/graphqlQueries.js';
import { IoTrashOutline } from "react-icons/io5";

function DeleteVaccination({ id }) {
    const [deleteVaccination] = useMutation(DELETE_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY]
    });

    return (
        <Button size="sm" onPress={() => deleteVaccination({ variables: { id: id } })}>
            <IoTrashOutline className="h-4 w-4" />
        </Button>
    )
}

export default DeleteVaccination;