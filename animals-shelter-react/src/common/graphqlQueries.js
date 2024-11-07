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
            vaccinationCount
        }}
`;

export const ADD_ANIMAL = gql`
    mutation ($name: String!,
        $species: String!,
        $primaryColor: String!,
        $breed: String,
        $implantChipId: String,
        $gender: String!,
        $birthDate: String,
        $pattern: String)
    {
        addAnimal(name: $name,
            species: $species,
            primaryColor:  $primaryColor,
            breed: $breed,
            implantChipId: $implantChipId,
            gender: $gender,
            birthDate: $birthDate,
            pattern: $pattern)
        {
            id
            name
            species
            primaryColor
            breed
            implantChipId
            gender
            birthDate
            pattern
        }
    }
`;

export const DELETE_ANIMAL = gql`
           mutation ($id: ID!, $reason: String!) {
               deleteAnimal(id: $id, reason: $reason)
               {
                 id
               }
           }
       `;
export const UPDATE_ANIMAL = gql`
           mutation UpdateAnimal($id: ID!,
                                 $primaryColor: String,
                                 $breed: String,
                                 $gender: String,
                                 $birthDate: String,
                                 $pattern: String) {
               updateAnimal(id: $id,
                            primaryColor: $primaryColor,
                            breed: $breed,
                            gender: $gender,
                            birthDate: $birthDate,
                            pattern: $pattern) {
                                 id
                                 name
                                 species
                                 primaryColor
                                 breed
                                 gender
                                 birthDate
                                 pattern
               }
           }
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

export const ALL_VACCINATIONS_QUERY = gql`
    {
        allVaccinations {
            id
            vaccine
            batch
            vaccinationTime
            comments
            email
            animal {
                id
                name
                species
            }
        }
    }
`;

export const DELETE_VACCINATION = gql`
    mutation ($id: ID!)
    {
        deleteVaccination(id: $id)
        {
            id
            vaccine
            batch
        }
    }
`;

export const ADD_VACCINATION = gql`
    mutation (
        $animalId: ID!,
        $vaccine: String!,
        $batch: String!,
        $vaccinationTime: String!,
        $comments: String,
        $email: String!)
    {
        addVaccination(
            animalId: $animalId,
            vaccine:  $vaccine,
            batch: $batch,
            vaccinationTime: $vaccinationTime,
            comments: $comments,
            email:  $email)
        {
            id
            vaccine
            batch
            vaccinationTime
            comments
            email
        }
    }
`;

export const UPDATE_VACCINATION = gql`
    mutation UpdateVaccination($id: ID!,
                              $vaccine: String,
                              $batch: String,
                              $vaccinationTime: String,
                              $comments: String,
                              $email: String) {
        updateVaccination(id: $id,
                           vaccine: $vaccine,
                           batch: $batch,
                           vaccinationTime: $vaccinationTime,
                           comments: $comments,
                           email: $email) {
            id
            vaccine
            batch
            vaccinationTime
            comments
            email
        }
    }
`;

export const GET_CURRENT_USER_PROFILE = gql`
  query {
    currentUserProfile {
      id
      name
      email
      roles
      animalNotifyStatus
    }
  }
`;