import { z } from "zod";
import { Material } from "./Material";

export interface MaterialGroup {
  id: number;
  name: string;
  displayOrder: number;
  description: string | null;
  materials: Array<Material>;
}

export const MaterialGroupEditSchema = z.object({
  name: z.string().min(1, "Name is required").max(64, "Name must be less than 64 characters"),
  description: z.string().max(512, "Description must be less than 512 characters").optional(),
  displayOrder: z.coerce
    .number({
      invalid_type_error: "Display order must be a number",
    })
    .min(0, "Display order must be greater than or equal to 0")
    .or(z.literal("")),
});

export const MaterialGroupCreateSchema = MaterialGroupEditSchema.merge(z.object({
  courseYearId: z.coerce.number({
    invalid_type_error: "Course year ID must be a number",
    required_error: "Course year ID is required",
  }),
}));

export type MaterialGroupCreateData = z.infer<typeof MaterialGroupCreateSchema>;
export type MaterialGroupEditData = z.infer<typeof MaterialGroupEditSchema>;
