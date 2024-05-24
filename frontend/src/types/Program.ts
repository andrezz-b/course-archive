import { z } from "zod";
import { College } from "./College";
import { SortDirection } from "./Common";

export interface Program {
  id: number;
  name: string;
  duration: number;
  degreeType?: string | null;
  degreeTitle?: string | null;
  degreeTitleAbbreviation?: string | null;
  description?: string | null;
  College?: College;
}

export enum ProgramFilterField {
  NAME = "name",
  DURATION = "duration",
}

export enum ProgramSortField {
  NAME = "name",
  DURATION = "duration",
}

export const ProgramSort = [
  {
    field: ProgramSortField.NAME,
    direction: SortDirection.ASC,
    label: "Name: A to Z",
    value: `${ProgramSortField.NAME}-${SortDirection.ASC}`,
  },
  {
    field: ProgramSortField.NAME,
    direction: SortDirection.DESC,
    label: "Name: Z to A",
    value: `${ProgramSortField.NAME}-${SortDirection.DESC}`,
  },
  {
    field: ProgramSortField.DURATION,
    direction: SortDirection.ASC,
    label: "Duration: Short to Long",
    value: `${ProgramSortField.DURATION}-${SortDirection.ASC}`,
  },
  {
    field: ProgramSortField.DURATION,
    direction: SortDirection.DESC,
    label: "Duration: Long to Short",
    value: `${ProgramSortField.DURATION}-${SortDirection.DESC}`,
  },
];

export const ProgramFilter = [
  {
    field: ProgramFilterField.NAME,
    label: "Name",
  },
  {
    field: ProgramFilterField.DURATION,
    label: "Duration",
  },
];

export const ProgramEditSchema = z.object({
  duration: z
    .preprocess(
      (args) => (args === "" ? undefined : args),
      z.coerce
        .number({ invalid_type_error: "Duration must be a number" })
        .positive("Duration must be positive"),
    )
    .optional() as z.ZodType<number, z.ZodTypeDef, number>,
  degreeType: z.string().max(64, "Must be less than 64 characters").optional(),
  degreeTitle: z.string().max(64, "Must be less than 64 characters").optional(),
  degreeTitleAbbreviation: z.string().max(64, "Must be less than 64 characters").optional(),
  description: z.string().max(512, "Must be less than 512 characters").optional(),
});
export const ProgramCreateSchema = z
  .object({
    name: z
      .string()
      .min(1, "Program name is required")
      .max(128, "Program name must be less than 128 characters"),
    collegeId: z.preprocess(
      (args) => (args === "" ? undefined : args),
      z.coerce
        .number({ invalid_type_error: "College id must be a number" })
        .positive("CollegId must be positive"),
    ) as z.ZodType<number, z.ZodTypeDef, number>,
  })
  .merge(ProgramEditSchema);

export type ProgramCreateData = z.infer<typeof ProgramCreateSchema>;
export type ProgramEditData = z.infer<typeof ProgramEditSchema>;
