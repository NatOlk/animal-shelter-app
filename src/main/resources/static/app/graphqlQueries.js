import { gql } from '@apollo/client';

export const ANIMALS_QUERY = gql`
    {
        allAnimals {
            id
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
    query vaccinationByAnimalId($animalId: ID!) {
        vaccinationByAnimalId(animalId: $animalId) {
            id
            vaccine
            batch
            vaccination_time
            comments
            email
        }
    }
`;
