import React, { useState, useEffect } from "react";
import { gql } from "graphql-tag";
import { useMutation } from "@apollo/client";
import { ANIMALS_QUERY } from '../graphqlQueries.js';
import M from 'materialize-css';

const DELETE_ANIMAL = gql`
    mutation ($id: ID!, $reason: String!) {
        deleteAnimal(id: $id, reason: $reason)
        {
          id
        }
    }
`;

function DeleteAnimal({ id }) {
    const [reason, setReason] = useState("");
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

    useEffect(() => {
        const elems = document.querySelectorAll('.modal');
        M.Modal.init(elems);
    }, []);

    const handleDelete = () => {
        deleteAnimal({ variables: { id, reason } })
            .catch((error) => {
                console.error("Error deleting animal:", error);
            });
    };

    const handleCancel = () => {
        const modalElem = document.querySelector('#modal1');
        const modalInstance = M.Modal.getInstance(modalElem);
        modalInstance.close();
        setReason("");
    };

    if (loading) return <p>Deleting...</p>;
    if (error) return <p>Error: {error.message}</p>;

    return (
        <>
            <button data-target="modal1" className="waves-effect waves-light btn modal-trigger">
                Delete
            </button>

            <div id="modal1" className="modal">
                <div className="modal-content">
                    <h4>Reason for Deletion</h4>
                    <p>Please set the reason for deletion</p>
                    <textarea value={reason} onChange={(e) => setReason(e.target.value)} />
                </div>
                <div className="modal-footer">
                    <a onClick={handleDelete} className="modal-close waves-effect waves-green btn-flat">Confirm</a>
                    <a onClick={handleCancel} className="modal-close waves-effect waves-red btn-flat">Cancel</a>
                </div>
            </div>
        </>
    );
}

export default DeleteAnimal;
