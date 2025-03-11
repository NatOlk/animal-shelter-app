import React, { useState, useEffect } from "react";
import { useMutation } from "@apollo/client";
import { ANIMALS_QUERY, DELETE_ANIMAL } from "../common/graphqlQueries";
import { Button, Progress, Textarea } from "@nextui-org/react";
import { IoTrashOutline } from "react-icons/io5";
import {
    Modal,
    ModalContent,
    ModalHeader,
    ModalBody,
    ModalFooter
} from "@nextui-org/modal";
import { DeleteProps } from "../common/types";

const DeleteAnimal: React.FC<DeleteProps> = ({ id, onError }) => {
    const [reason, setReason] = useState<string>("Adopted");
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

    const [deleteAnimal, { loading, error }] = useMutation(DELETE_ANIMAL, {
            refetchQueries: [{ query: ANIMALS_QUERY }],
        });

    useEffect(() => {
        if (error) {
            onError(`Failed to delete animal: ${error.message}`);
        }
    }, [error, onError]);

    if (loading) {
        return (
            <div className="flex justify-center items-center h-full">
                <Progress isIndeterminate aria-label="Deleting animal" size="lg" />
            </div>
        );
    }

    const handleDelete = async () => {
        try {
            await deleteAnimal({ variables: { id, reason } });
            setIsModalOpen(false);
            setReason("Adopted");
        } catch (error) {
            setIsModalOpen(false);
            onError("Failed to delete animal: " + (error as Error).message);
        }
    };

    return (
        <>
            <Button
                variant="light"
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
                        <Textarea
                            className="max-w-xs"
                            value={reason}
                            isClearable
                            variant="bordered"
                            aria-label="Reason for removal"
                            onClear={() => setReason("")}
                            onChange={(e) => setReason(e.target.value)}
                            isRequired/>
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
};

export default DeleteAnimal;
