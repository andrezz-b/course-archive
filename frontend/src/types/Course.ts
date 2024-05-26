import { z } from "zod";
import { SortDirection } from "./Common";
import { Program } from "./Program";

export interface Course {
  id: number;
  name: string;
  credits: number;
  year: number | null;
  acronym: string | null;
  description: string | null;
  program?: Program;
}

export enum CourseFilterField {
  NAME = "name",
  CREDITS = "credits",
  YEAR = "year",
  ACROYNM = "acronym",
}

export enum CourseSortField {
  NAME = "name",
}

export const CourseSort = [
  {
    field: CourseSortField.NAME,
    direction: SortDirection.ASC,
    label: "Name: A to Z",
    value: `${CourseSortField.NAME}-${SortDirection.ASC}`,
  },
  {
    field: CourseSortField.NAME,
    direction: SortDirection.DESC,
    label: "Name: Z to A",
    value: `${CourseSortField.NAME}-${SortDirection.DESC}`,
  },
];

export const CourseFilter = [
  {
    field: CourseFilterField.NAME,
    label: "Name",
  },
  {
    field: CourseFilterField.CREDITS,
    label: "Credits",
  },
  {
    field: CourseFilterField.YEAR,
    label: "Year",
  },
  {
    field: CourseFilterField.ACROYNM,
    label: "Acronym",
  },
];

export const CourseEditSchema = z.object({
  name: z.string().min(1, "Name is required").max(128, "Name must be at most 128 characters"),
  credits: z.coerce
    .number({
      invalid_type_error: "Credits must be a number",
      required_error: "Credits is required",
    })
    .positive("Credits must be positive"),
  year: z.coerce
    .number({
      invalid_type_error: "Year must be a number",
    })
    .positive("Year must be positive")
    .optional(),
  acronym: z.string().max(32, { message: "Acronym must be at most 32 characters" }).optional(),
  description: z
    .string()
    .max(512, { message: "Description must be at most 512 characters" })
    .optional(),
});

export const CourseCreateSchema = CourseEditSchema.merge(z
  .object({
    programId: z.coerce
      .number({
        invalid_type_error: "Must be a number",
        required_error: "Program id is required",
      })
      .min(1, "Program id must be at least 1"),
  }));
  

export type CourseCreateData = z.infer<typeof CourseCreateSchema>;
export type CourseEditData = z.infer<typeof CourseEditSchema>;