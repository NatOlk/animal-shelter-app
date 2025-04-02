import React, { useState, useEffect } from "react";
import {
    Input, Button, Spacer, Alert, Progress
} from "@nextui-org/react";
import { Image } from "@nextui-org/image";
import { Divider } from "@nextui-org/divider";
import { Card, CardHeader, CardBody } from "@nextui-org/card";
import { FaRegTrashAlt } from "react-icons/fa";
import { useParams } from "react-router-dom";
import { useQuery, useMutation } from "@apollo/client";
import { useConfig } from "../common/configContext";
import { apiFetch } from '../common/api';
import { ANIMAL_BY_ID_QUERY, UPDATE_ANIMAL } from "../common/graphqlQueries";
import { Animal, Config } from "../common/types";

const AnimalDetails: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [file, setFile] = useState<File | null>(null);
    const [message, setMessage] = useState<string>("");
    const [forceUpdate, setForceUpdate] = useState<number>(Date.now());
    const config: Config | null = useConfig();

    const { loading, error, data } = useQuery<{ animalById: Animal }>(ANIMAL_BY_ID_QUERY, {
        variables: { id },
        fetchPolicy: "network-only",
    });

    const [photoImgPath, setPhotoImgPath] = useState<string | null>(null);

    useEffect(() => {
        if (data?.animalById?.photoImgPath) {
            setPhotoImgPath(data.animalById.photoImgPath);
        }
    }, [data]);

    const [updateAnimal] = useMutation(UPDATE_ANIMAL, {
        onCompleted: () => {
            setPhotoImgPath(null);
            setMessage("Image deleted successfully!");
        },
        onError: (error) => {
            setMessage("Error deleting image.");
        }
    });

    if (loading) {
        return (
            <div>
                <p>Loading...</p>
                <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
            </div>
        );
    }

    if (error) {
        return (
            <Alert dismissable color="danger" variant="bordered" title={`Error: ${error.message}`} />
        );
    }

    const animal = data?.animalById;

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            setFile(event.target.files[0]);
        }
    };

    const handleUpload = async () => {
        if (!file) {
            setMessage("Please select a file.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);
        formData.append("species", animal.species);
        formData.append("name", animal.name);
        formData.append("breed", animal.breed);
        formData.append("birthDate", animal.birthDate);

        try {
            const response = await apiFetch(`/api/${id}/upload-photo`, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                const filename = await response.text();
                setPhotoImgPath(filename);
                setForceUpdate(Date.now());
                setMessage("Image uploaded successfully!");
            } else {
                setMessage("Error uploading file.");
            }
        } catch (error) {
           setMessage("Error uploading file.");
        }
    };

    const handleDeletePhoto = async () => {
        try {
            await updateAnimal({
                variables: {
                    animal: {
                        id: animal.id,
                        photoImgPath: ""
                    }
                }
            });

            setPhotoImgPath(null);
            setMessage("Image deleted successfully!");

        } catch (error) {
            setMessage("Failed to delete image.");
        }
    };

    return (
        <div>
            <div className="containerProfile">
                <div className="profileCard">
                    <Card className="w-full">
                        <CardHeader className="flex gap-3">
                            <div className="flex flex-col">
                                <h1>Animal Details</h1>
                            </div>
                        </CardHeader>
                        <Divider />
                        <CardBody>
                            <div>
                                <p><strong>Name:</strong> {animal.name}</p>
                                <p><strong>Species:</strong> {animal.species}</p>
                                <p><strong>Primary Color:</strong> {animal.primaryColor}</p>
                                <p><strong>Breed:</strong> {animal.breed}</p>
                                <p><strong>Gender:</strong> {animal.gender}</p>
                                <p><strong>Birth Date:</strong> {animal.birthDate}</p>
                                <p><strong>Pattern:</strong> {animal.pattern}</p>
                                <p><strong>Implant Chip ID:</strong> {animal.implantChipId}</p>
                            </div>
                            <Spacer y={10} />
                            {photoImgPath && (
                                <div className="flex items-center gap-4">
                                    <Image
                                        isZoomed
                                        alt="Animal Image"
                                        src={`${config.animalShelterApp}${photoImgPath}?timestamp=${forceUpdate}`}
                                        width={350}
                                        height={350}
                                        radius="md"
                                    />
                                    <Button color="danger" variant="light" onPress={handleDeletePhoto}>
                                        <FaRegTrashAlt size={20} /> Delete Image
                                    </Button>
                                </div>
                            )}
                            <Spacer y={10} />
                            <div className="flex items-center gap-3">
                                <h3>Upload a new image (will replace the current one)</h3>
                                <Input
                                    type="file"
                                    isRequired
                                    className="max-w-xs"
                                    variant="bordered"
                                    onChange={handleFileChange}
                                    aria-label="Upload Animal Image"
                                />
                                <Button color="default" variant="flat" onPress={handleUpload}>
                                    Upload
                                </Button>
                            </div>
                            {message && <p style={{ color: "red" }}>{message}</p>}
                        </CardBody>
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default AnimalDetails;
