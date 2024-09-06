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
