import {z} from "zod";

export interface Tag {
  id: number;
  name: string;
  createdAt: string;
  updatedAt: string;
}

export const TagCreateSchema = z.object({
  name: z.string().min(1, "Name must not be empty"),
  courseYearId: z.number().positive(),
});

export const TagEditSchema = TagCreateSchema.omit({ courseYearId: true });

export type TagCreateData = z.infer<typeof TagCreateSchema>;
export type TagEditData = z.infer<typeof TagEditSchema>;
