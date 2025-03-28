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
        }
    }
`;

export const ANIMAL_BY_ID_QUERY = gql`
 query animalById($id: ID!) {
        animalById(id: $id) {
            id
            name
            species
            primaryColor
            implantChipId
            breed
            gender
            admissionDate
            birthDate
            pattern
            vaccinationCount
            photoImgPath
        }
    }
`;

export const ADD_ANIMAL = gql`
    mutation AddAnimal($animal: AnimalInput!) {
        addAnimal(animal: $animal) {
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
    mutation DeleteAnimal($id: ID!, $reason: String!) {
        deleteAnimal(id: $id, reason: $reason) {
            id
        }
    }
`;

export const UPDATE_ANIMAL = gql`
    mutation UpdateAnimal($animal: UpdateAnimalInput!) {
        updateAnimal(animal: $animal) {
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
    mutation DeleteVaccination($id: ID!) {
        deleteVaccination(id: $id) {
            id
            vaccine
            batch
        }
    }
`;

export const ADD_VACCINATION = gql`
    mutation AddVaccination($vaccination: VaccinationInput!) {
        addVaccination(vaccination: $vaccination) {
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
    mutation UpdateVaccination($vaccination: UpdateVaccinationInput!) {
        updateVaccination(vaccination: $vaccination) {
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

export const UPDATE_USER_ROLES = gql`
  mutation UpdateUserRoles($username: String!, $roles: [String!]!) {
    updateUserRoles(username: $username, roles: $roles) {
      id
      name
      email
      roles
    }
  }
`;

export const GET_NON_ADMIN_USERS = gql`
  query {
    allNonAdminUsers {
      id
      name
      email
      roles
      animalNotifyStatus
    }
  }
`;