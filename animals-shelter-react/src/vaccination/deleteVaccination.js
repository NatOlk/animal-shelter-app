import React from "react";
import { useMutation } from "@apollo/client";
import { Button } from "@nextui-org/react";
import { VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY, DELETE_VACCINATION } from '../common/graphqlQueries.js';
import { IoTrashOutline } from "react-icons/io5";

function DeleteVaccination({ id, onError }) {
    const [deleteVaccination] = useMutation(DELETE_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY]
    });

 const handleDelete = () => {
        deleteVaccination({ variables: { id } })
            .then(() => {
            })
            .catch((error) => {
                if (onError) {
                    onError("Failed to delete vaccination: " + error.message);
                }
            });
    };

    return (
        <Button variant="light"
            className="p-2 min-w-2 h-auto"
            onPress={handleDelete}>
            <IoTrashOutline className="h-4 w-4" />
        </Button>
    )
}

export default DeleteVaccination;