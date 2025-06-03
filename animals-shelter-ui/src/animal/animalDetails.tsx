import React, { useState, useEffect } from "react";
import {
    Input, Button, Spacer, Alert, Progress,
    Table, TableCell, TableColumn, TableRow, TableHeader, TableBody
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
import EditableAnimalField from "./editableAnimalField";
import VaccinationsList from "../vaccination/vaccinationsList";

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
        onError: () => {
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
        } catch {
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
        } catch {
            setMessage("Failed to delete image.");
        }
    };

    return (
        <div>
            <div className="containerProfile">
                <div className="profileCard">
                    <Card className="w-full">
                        <CardHeader className="flex gap-3 card-header-main">
                            <div className="flex flex-col">
                                <h4 className="text-medium font-medium">Animal Details</h4>
                            </div>
                        </CardHeader>
                        <Divider />
                        <CardBody>
                            <Table className="compact-table">
                                <TableHeader>
                                    <TableColumn className="w-full w-32">#</TableColumn>
                                    <TableColumn></TableColumn>
                                    <TableColumn className="w-full w-32">#</TableColumn>
                                    <TableColumn></TableColumn>
                                </TableHeader>
                                <TableBody>
                                    <TableRow className="table-row">
                                        <TableCell><h1>Name:</h1></TableCell>
                                        <TableCell>{animal.name}</TableCell>
                                        <TableCell><h1>Species:</h1></TableCell>
                                        <TableCell>{animal.species}</TableCell>
                                    </TableRow>
                                    <TableRow className="table-row">
                                        <TableCell><h1>Primary color:</h1></TableCell>
                                        <TableCell><EditableAnimalField
                                            entity={animal} value={animal.primaryColor} name="primaryColor"
                                            values={config?.colors} />
                                        </TableCell>
                                        <TableCell><h1>Breed:</h1></TableCell>
                                        <TableCell><EditableAnimalField
                                            entity={animal} value={animal.breed} name="breed" />
                                        </TableCell>
                                    </TableRow>
                                    <TableRow className="table-row">
                                        <TableCell><h1>Gender:</h1></TableCell>
                                        <TableCell><EditableAnimalField
                                            entity={animal} value={animal.gender} name="gender"
                                            values={config?.genders} />
                                        </TableCell>
                                        <TableCell><h1>Birth date:</h1></TableCell>
                                        <TableCell><EditableAnimalField
                                            entity={animal} value={animal.birthDate} name="birthDate" isDate={true} />
                                        </TableCell>
                                    </TableRow>
                                    <TableRow className="table-row">
                                        <TableCell><h1>Pattern:</h1></TableCell>
                                        <TableCell><EditableAnimalField
                                            entity={animal} value={animal.pattern} name="pattern" />
                                        </TableCell>
                                        <TableCell><h1>Implant chip ID:</h1></TableCell>
                                        <TableCell><EditableAnimalField
                                            entity={animal} value={animal.implantChipId}
                                            name="implantChipId" />
                                        </TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                            <Card className="w-full">
                                <Divider />
                                <CardBody>
                                    <Spacer y={10} />
                                    {
                                        photoImgPath && (
                                            <div className="flex items-center gap-4">
                                                <Image
                                                    isZoomed
                                                    alt="Animal Image"
                                                    src={`${photoImgPath}?timestamp=${forceUpdate}`}
                                                    width={350}
                                                    height={350}
                                                    radius="md" />
                                                <Button color="danger" variant="light" onPress={handleDeletePhoto}>
                                                    <FaRegTrashAlt size={20} /> Delete Image
                                                </Button>
                                            </div>
                                        )
                                    }
                                    <Spacer y={10} />
                                    <div className="flex items-center gap-3">
                                        <h3>Upload a new image (will replace the current one)</h3>
                                        <Input
                                            type="file"
                                            isRequired
                                            className="max-w-xs"
                                            variant="bordered"
                                            onChange={handleFileChange}
                                            aria-label="Upload Animal Image" />
                                        <Button color="default" variant="flat" onPress={handleUpload}>
                                            Upload
                                        </Button>
                                    </div>
                                    {message && <p style={{ color: "red" }}>{message}</p>}
                                </CardBody>
                            </Card>
                            <Spacer y={10}/>
                            <Card className="w-full">
                              <Divider />
                                <CardHeader className="flex gap-3 card-header-detail1">
                                    <div className="flex flex-col">
                                        <h4 className="text-medium font-medium">Medical information</h4>
                                    </div>
                                </CardHeader>
                                <CardBody>
                                    <Divider />
                                     <VaccinationsList animalId={id} />
                                </CardBody>
                            </Card>
                        </CardBody>
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default AnimalDetails;
