import { z } from "zod";
import { SortDirection } from "./Common";

export interface College {
  id: number;
  name: string;
  acronym?: string;
  city: string;
  postcode: number;
  address: string;
  website?: string;
  description?: string;
}

export enum CollegeFilterField {
  NAME = "name",
  CITY = "city",
  POSTCODE = "postcode",
  ACROYNM = "acronym",
}

export enum CollegeSortField {
  NAME = "name",
}

export const CollegeSort = [
  {
    field: CollegeSortField.NAME,
    direction: SortDirection.ASC,
    label: "Name: A to Z",
    value: `${CollegeSortField.NAME}-${SortDirection.ASC}` as SortValue,
  },
  {
    field: CollegeSortField.NAME,
    direction: SortDirection.DESC,
    label: "Name: Z to A",
    value: `${CollegeSortField.NAME}-${SortDirection.DESC}` as SortValue,
  },
];

export type SortValue = `${CollegeSortField}-${SortDirection}`;

export const CollegeFilter = [
  {
    field: CollegeFilterField.NAME,
    label: "Name",
  },
  {
    field: CollegeFilterField.CITY,
    label: "City",
  },
  {
    field: CollegeFilterField.POSTCODE,
    label: "Postcode",
  },
  {
    field: CollegeFilterField.ACROYNM,
    label: "Acronym",
  },
];

export const CollegeCreateSchema = z.object({
  name: z
    .string()
    .min(1, { message: "College name is required" })
    .max(128, "College name must be at most 128 characters"),
  acronym: z.string().optional(),
  city: z.string().min(1, { message: "City is required" }),
  postcode: z.preprocess(
    (args) => (args === "" ? undefined : args),
    z.coerce
      .number({ invalid_type_error: "Postcode must be a number" })
      .positive("Postcode must be positive"),
  ),
  address: z.string().min(1, { message: "Address is required" }),
  website: z.string().url().optional().or(z.literal('')),
  description: z.string().max(512, "Description must be at most 512 characters").optional(),
});

export const CollegeEditSchema = z.object({
  acronym: z.string().optional(),
  city: z.string().min(1, { message: "City is required" }),
  postcode: z.preprocess(
    (args) => (args === "" ? undefined : args),
    z.coerce
      .number({ invalid_type_error: "Postcode must be a number" })
      .positive("Postcode must be positive"),
  ),
  address: z.string().min(1, { message: "Address is required" }),
  website:  z.string().url().optional().or(z.literal('')),
  description: z.string().max(512, "Description must be at most 512 characters").optional(),
});

export type CollegeCreateData = z.infer<typeof CollegeCreateSchema>;
export type CollegeEditData = z.infer<typeof CollegeEditSchema>;
