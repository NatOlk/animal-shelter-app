import React, {ReactNode } from "react";

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

interface Vaccination {
  id: string;
  vaccine: string;
  batch: string;
  vaccinationTime: string;
  comments?: string;
  email: string;
  animal: {
    name: string;
    species: string;
  };
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

export interface EditableAnimalFieldProps {
  animal: Animal;
  name: string;
  value: string | number;
  values?: string[];
  isDate?: boolean;
}

interface EditableVaccineFieldProps {
    vaccination: Vaccination;
    [key: string]: any;
}

interface EditableFieldBaseProps {
    entity: { id: string };
    value: string;
    name: string;
    values?: string[];
    isDate?: boolean;
    updateField: (variables: { variables: Record<string, any> }) => Promise<void>;
}

interface Subscriber {
  id: string;
  email: string;
  approver: string;
  topic: string;
  accepted: boolean;
  approved: boolean;
  token: string;
}

interface User {
    id: string | null;
    email: string | null;
}

interface AuthContextType {
    isAuthenticated: boolean;
    user: User;
    login: (userData: User, token: string) => void;
    logout: () => void;
    isLoading: boolean;
}

interface SubscriptionStatusProps {
  status: 'NONE' | 'PENDING' | 'ACTIVE' | null;
}

interface UserProfileData {
  currentUserProfile: {
    name: string;
    email: string;
    animalNotifyStatus?: SubscriptionStatusProps;
  };
}

interface DateFieldProps {
    onDateChange?: (date: any) => void;
}

interface ChildrenProps {
    children: ReactNode;
}

interface SubscriptionListProps {
  userProfile: UserProfile;
}

interface FetchOptions extends RequestInit {
  body?: any;
}