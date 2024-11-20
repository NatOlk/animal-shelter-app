import React, { useState, useEffect } from "react";
import { useMutation } from "@apollo/client";
import { ANIMALS_QUERY, DELETE_ANIMAL } from '../common/graphqlQueries.js';
import M from 'materialize-css';

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
            <button data-target="modal1" className="waves-effect waves-light modal-trigger btn-small">
                <i className="material-icons">close</i>
            </button>

            <div id="modal1" className="modal">
                <div className="modal-content">
                    <h5>Reason for removal</h5>
                    <textarea value={reason}
                        onChange={(e) => setReason(e.target.value)}
                        className="removal materialize-textarea" />
                </div>
                <div className="modal-footer">
                    <button
                        tabIndex={0}
                        onClick={handleDelete}
                        onKeyDown={(e) => { if (e.key === 'Enter') handleDelete(); }}
                        className="modal-close waves-effect waves-orange btn-flat">
                        Confirm
                    </button>
                    <button
                        tabIndex={0}
                        onClick={handleCancel}
                        onKeyDown={(e) => { if (e.key === 'Enter') handleCancel(); }}
                        className="modal-close waves-effect waves-red btn-flat">
                        Cancel
                    </button>

                </div>
            </div >
        </>
    );
}

export default DeleteAnimal;
