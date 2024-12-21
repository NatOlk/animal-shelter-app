import React, { useState } from "react";
import { useMutation } from "@apollo/client";
import { ANIMALS_QUERY, DELETE_ANIMAL } from '../common/graphqlQueries.js';
import { Button } from "@nextui-org/react";
import {
    Modal,
    ModalContent,
    ModalHeader,
    ModalBody,
    ModalFooter
} from "@nextui-org/modal";
import { Textarea } from "@nextui-org/input";
import { IoTrashOutline } from "react-icons/io5";

function DeleteAnimal({ id }) {
    const [reason, setReason] = useState("");
    const [isModalOpen, setIsModalOpen] = useState(false);

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

    const handleDelete = () => {
        deleteAnimal({ variables: { id, reason } })
            .then(() => {
                setIsModalOpen(false);
                setReason("");
            })
            .catch((error) => console.error("Error deleting animal:", error));
    };

    return (
        <>
            <Button variant="light"
                className="p-2 min-w-2 h-auto"
                onPress={() => setIsModalOpen(true)}>
                <IoTrashOutline />
            </Button>
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <ModalContent>
                    <ModalHeader className="flex flex-col gap-1">
                        Reason for removal
                    </ModalHeader>
                    <ModalBody>
                        <Textarea className="max-w-xs"
                            value={reason}
                            defaultValue="Adopted"
                            isClearable
                            variant="bordered"
                            onClear={() => console.log("textarea cleared")}
                            onChange={(e) => setReason(e.target.value)}
                            isRequired
                        />
                    </ModalBody>
                    <ModalFooter>
                        <Button color="default" variant="bordered" size="sm" onPress={() => setIsModalOpen(false)}>
                            Cancel
                        </Button>
                        <Button color="default" variant="bordered" size="sm" onPress={handleDelete}>
                            Confirm
                        </Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </>
    );
}

export default DeleteAnimal;
