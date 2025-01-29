import React, { useState, useEffect } from "react";
import {
    Input, Button, Select, SelectItem, Spacer,
    Alert, Progress
} from "@nextui-org/react";
import { useParams } from "react-router-dom";
import { useQuery } from "@apollo/client";
import { apiFetch } from '../common/api';
import { ANIMAL_BY_ID_QUERY } from "../common/graphqlQueries.js";

function AnimalDetails() {
    const { id } = useParams();
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState("");
    const { loading, error, data, refetch } = useQuery(ANIMAL_BY_ID_QUERY, {
        variables: { id },
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
            <Alert
                dismissable
                color="danger"
                variant="bordered"
                onClose={() => setGlobalError("")}
                title={`Error: ${error.message}`}
            />
        );
    }

    const animal = data?.animalById;

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        if (!file) {
            setMessage("Please select a file.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
             const response = await apiFetch(`/${id}/upload-photo`, {
                method: 'POST',
                body: formData
            });

            setMessage(response.data);
        } catch (error) {
            console.error("Error uploading file:", error);
            setMessage("Error uploading file.");
        }
    };

    return (
        <div>
            <h1>Animal Details</h1>
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
            <div>
                <h3>Upload Animal Image</h3>
                <Input
                    type="file"
                    onChange={handleFileChange}
                    aria-label="Upload Animal Image" />
                <Button color="primary" onPress={handleUpload}>
                    Upload Image
                </Button>
                {animal.photoImgPath && (
                    <div>
                        <h4>Current Image</h4>
                        <img src={animal.photoImgPath} width={200} />
                    </div>
                )}
            </div>
        </div>
    );
}

export default AnimalDetails;
