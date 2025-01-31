import React, { useEffect } from "react";
import { useMutation } from "@apollo/client";
import { Button, Progress } from "@nextui-org/react";
import { VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY, DELETE_VACCINATION } from '../common/graphqlQueries';
import { IoTrashOutline } from "react-icons/io5";

function DeleteVaccination({ id, onError }) {

      const [deleteVaccination, { loading, error }]  = useMutation(DELETE_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY]
    });

    useEffect(() => {
        if (error && onError) {
            onError(`Failed to delete vaccination: ${error.message}`);
        }
    }, [error, onError]);

    if (loading) {
        return (
            <div className="flex justify-center items-center h-full">
                <Progress
                    isIndeterminate
                    aria-label="Deleting animal"
                    size="lg"
                />
            </div>
        );
    }

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