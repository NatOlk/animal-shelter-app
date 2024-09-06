import React, { useState }  from 'react';
import {Container, Table} from 'reactstrap';

import {gql} from 'graphql-tag';
import { useQuery } from "@apollo/client";
import DeleteVaccination from "./deleteVaccination";
import AddVaccination from "./addVaccination";
import UpdateVaccination from "./updateVaccination";
import { useLocation, useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';


const VACCINATIONS_QUERY = gql`
    query vaccinationByNameAndSpecies($name: String!, $species: String!) {
        vaccinationByNameAndSpecies(name: $name, species: $species) {
            name
            species
            vaccine
            batch
            vaccination_time
            comments
            email
        }
    }
`;
function VaccinationsList() {

    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);

    const location = useLocation();
    const { name, species } = location.state;


    if (!name || !species) {
        return (<div>
            <Link to="/">Back to Animals</Link>
            <p>Error: No name or species provided!</p>;
            </div>);
    }

    const {loading, error, data} = useQuery(VACCINATIONS_QUERY, {
                                       variables: { name: name, species:species },
                                   });

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error :(</p>;

    const pageCount = Math.ceil(data.vaccinationByNameAndSpecies.length / perPage);

    const vaccinationsList = data.vaccinationByNameAndSpecies
        .slice(currentPage * perPage, (currentPage + 1) * perPage)
        .map(vaccination => <UpdateVaccination key={vaccination.name + vaccination.species + vaccination.vaccine + vaccination.batch} vaccination={vaccination} />);

    return (
        <div>
            <div id="error" className="errorAlarm"></div>
            <Link to="/">Back to Animals</Link>
            <Container fluid>
                <h3>Vaccinations</h3>

                <Table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Species</th>
                        <th>Vaccine</th>
                        <th>Batch</th>
                        <th>Vaccination time</th>
                        <th>Comments</th>
                        <th>Email</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                      <AddVaccination name={name} species={species}/>
                      {vaccinationsList}
                     </tbody>
                     </Table>
                    <div>
                        {
                         Array.from({length: pageCount}, (_, index) => (
                           <button key={index} className="round-button-with-border"
                              onClick={() => setCurrentPage(index)}>{index + 1}</button>
                         ))
                         }
                    </div>
            </Container>
        </div>
    );
}

export default VaccinationsList;
