import { PermissionNameType, Role } from "@/types/Auth.ts";
import { ObjectType } from "@/types/Common.ts";

export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  roles: Array<UserRole>;
}

interface UserRole {
  id: number;
  name: Role;
}

export type ObjectPermission = Record<
  PermissionNameType,
  {
    granted: boolean;
    grantedByParent: boolean;
    grantedByHigherPermission: boolean;
  }
>;

export interface GetObjectPermissionData {
  objectType: ObjectType;
  objectId: number;
  username: string;
}
