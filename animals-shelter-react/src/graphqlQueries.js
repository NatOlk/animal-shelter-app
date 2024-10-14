import { gql } from '@apollo/client';

export const ANIMALS_QUERY = gql`
    {
        allAnimals {
            id
            name
            species
            primaryColor
            breed
            implantChipId
            gender
            birthDate
            pattern
        }}
`;


export const VACCINATIONS_QUERY = gql`
    query vaccinationByAnimalId($animalId: ID!) {
        vaccinationByAnimalId(animalId: $animalId) {
            id
            vaccine
            batch
            vaccinationTime
            comments
            email
        }
    }
`;
