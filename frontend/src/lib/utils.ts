import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";
import type { ZodSchema } from 'zod';
import {User} from "@/types/User";
import {Role} from "@/types/Auth.ts";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function getDefinedValuesObject<T extends object>(obj: T): T {
  return Object.fromEntries(
    Object.entries(obj).filter(([, value]) => !!value)
  ) as T;
}

export function getZodSchemaFieldsShallow(schema: ZodSchema) {
  const fields: Record<string, true> = {};
  const proxy = new Proxy(fields, {
    get(_, key) {
      if (key === 'then' || typeof key !== 'string') {
        return;
      }
      fields[key] = true;
    },
  });
  schema.safeParse(proxy);
  return fields;
}

export function showAdminButton(user: User) {
  return !!user.roles.find((role) => role === Role.ADMIN || role === Role.MANAGER);
}
