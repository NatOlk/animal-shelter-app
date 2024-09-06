import { gql } from '@apollo/client';

export const ANIMALS_QUERY = gql`
    {
        allAnimals {
            name
            species
            primary_color
            breed
            implant_chip_id
            gender
            birth_date
            pattern
        }}
`;


export const VACCINATIONS_QUERY = gql`
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

export const GET_VACCINATION_COUNT = gql`
  query vaccinationCountByNameAndSpecies($name: String!, $species: String!) {
    vaccinationCountByNameAndSpecies(name: $name, species: $species)
  }
`;