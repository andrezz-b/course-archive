import {ObjectType} from "@/types/Common.ts";

interface StateContextType<T> {
  auth: T;
  setAuth: React.Dispatch<React.SetStateAction<T>>;
}

export interface AuthContextData {
  accessToken: string;
}

export type AuthContextType = StateContextType<AuthContextData | null>;

export interface AuthContextProps {
  children: React.ReactNode;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
}

export interface RefreshResponse {
  accessToken: string;
}

export interface RegisterData {
  email: string;
  password: string;
  passwordRepeat: string;
  firstName: string;
  lastName: string;
  username: string;
}

export interface LoginData {
  username: string;
  password: string;
}

export const Permission = {
  READ: "READ",
  WRITE: "WRITE",
  CREATE: "CREATE",
  DELETE: "DELETE",
  ADMINISTRATION: "ADMINISTRATION",
} as const;

export type PermissionName = (typeof Permission)[keyof typeof Permission];

export enum Role {
  USER = "USER",
  MANAGER = "MANAGER",
  ADMIN = "ADMIN",
}

export interface ChangePermissionData {
  objectType: ObjectType;
  objectId: number;
  username: string;
  permission: PermissionName;
  granting: boolean;
}
