import React from "react";
import {
    Spacer, Progress,
    Table, TableCell, TableColumn, TableRow, TableHeader, TableBody
} from "@nextui-org/react";
import { Image } from "@nextui-org/image";
import { Divider } from "@nextui-org/divider";
import { Card, CardHeader, CardBody } from "@nextui-org/card";
import { useParams } from "react-router-dom";
import { useQuery } from "@apollo/client";
import { ANIMAL_BY_ID_QUERY } from "../common/graphqlQueries";
import { Animal } from "../common/types";

const AnimalPublicDetails: React.FC = () => {
    const { id } = useParams<{ id: string }>();

    const { loading, error, data } = useQuery<{ animalById: Animal }>(ANIMAL_BY_ID_QUERY, {
        variables: { id },
        fetchPolicy: "network-only",
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
            <p style={{ color: "red" }}>{`Error: ${error.message}`}</p>
        );
    }

    const animal = data?.animalById;

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
                                        <TableCell>{animal.primaryColor}</TableCell>
                                        <TableCell><h1>Breed:</h1></TableCell>
                                        <TableCell>{animal.breed}</TableCell>
                                    </TableRow>
                                    <TableRow className="table-row">
                                        <TableCell><h1>Gender:</h1></TableCell>
                                        <TableCell>{animal.gender}</TableCell>
                                        <TableCell><h1>Birth date:</h1></TableCell>
                                        <TableCell>{animal.birthDate}</TableCell>
                                    </TableRow>
                                    <TableRow className="table-row">
                                        <TableCell><h1>Pattern:</h1></TableCell>
                                        <TableCell>{animal.pattern}</TableCell>
                                        <TableCell><h1>Implant chip ID:</h1></TableCell>
                                        <TableCell>{animal.implantChipId}</TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                            <Spacer y={10}/>
                            {animal.photoImgPath && (
                                <Card className="w-full">
                                    <Divider />
                                    <CardBody>
                                        <div className="flex items-center justify-center">
                                            <Image
                                                isZoomed
                                                alt="Animal Image"
                                                src={`${animal.photoImgPath}`}
                                                width={350}
                                                height={350}
                                                radius="md"/>
                                        </div>
                                    </CardBody>
                                </Card>
                            )}
                        </CardBody>
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default AnimalPublicDetails;
