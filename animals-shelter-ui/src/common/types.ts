import React, { ReactNode } from "react";

export interface Animal {
  id: string;
  name: string;
  species: string;
  primaryColor: string;
  breed?: string;
  gender: string;
  birthDate: string;
  implantChipId?: string;
  pattern?: string;
  vaccinationCount?: number;
}

export interface Vaccination {
  id: string;
  vaccine: string;
  batch: string;
  vaccinationTime: string;
  comments?: string;
  email: string;
  animal: Pick<Animal, 'name' | 'species'>;
}

export interface Config {
  species: string[];
  colors: string[];
  genders: string[];
  [key: string]: any;
}

export interface ConfigContextType extends Config {
  loading: boolean;
  error: Error | null;
}

export interface DeleteProps {
  id: string;
  onError: (error: string) => void;
}

export interface EditableFieldProps {
  entity: { id: string };
  name: string;
  value: string | number;
  values?: string[];
  isDate?: boolean;
  updateField: (variables: { variables: Record<string, any> }) => Promise<void>;
}

export interface Subscriber {
  id: string;
  email: string;
  approver: string;
  topic: string;
  accepted: boolean;
  approved: boolean;
  token: string;
}

export interface User {
  id: string | null;
  name: string | null;
  email: string | null;
  roles?: string[];
}

export interface AuthContextType {
  isAuthenticated: boolean;
  user: User;
  setUser: (user: User) => void;
  login: (userData: User, token: string) => void;
  logout: () => void;
  isLoading: boolean;
  isAdmin: boolean;
}

export interface SubscriptionStatusProps {
  status: 'NONE' | 'PENDING' | 'ACTIVE' | null;
}

export interface UserProfileData {
  currentUserProfile: {
    name: string;
    email: string;
    animalNotifyStatus?: SubscriptionStatusProps;
  };
}

export interface DateFieldProps {
  onDateChange?: (date: any) => void;
}

export interface ChildrenProps {
  children: ReactNode;
}

export interface SubscriptionListProps {
  userProfile: UserProfileData;
}

export interface FetchOptions extends RequestInit {
  body?: any;
}

export interface AddAnimalProps {
  config: Config;
  onError: (error: string) => void;
}

export interface AnimalRowProps {
  animal: Animal;
  config: Config;
  onError: (error: string) => void;
}

export interface VaccinationRowProps {
  vaccination: Vaccination;
  onError: (error: string) => void;
}

export interface AddVaccinationProps {
  config: Config;
  animalId: string;
  onError: (error: string) => void;
}

export interface RegisterResponse {
  email: string;
  message: string;
}

type CountByDate = {
  date: string
  count: number
}

type AnimalLifespan = {
  id: number
  name: string
  species: string
  daysInSystem: number
  hoursInSystem: number
  minutesInSystem: number
}

export const topics = [
{
  key: 'animalShelterNewsTopicId',
  label: 'Animal Shelter News',
  description: 'Get the latest announcements, stories, and shelter-wide news.'
},
{
  key: 'animalTopicId',
  label: 'Animal Information',
  description: 'Stay informed about new arrivals, adoptions, and animal removals.'
},
{
  key: 'vaccinationTopicId',
  label: 'Vaccination Updates',
  description: 'Receive notifications about newly added or removed vaccinations.'
},
] as const;


export type TopicKey = typeof topics[number]['key'];

export type TopicDecisionStats = {
  topic: string
  count: number
  approvedCount: number
  rejectedCount: number
}

export type TopicRequestStats = {
  topic: string,
  approver: string,
  count: number
}