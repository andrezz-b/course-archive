import {z} from "zod";

export interface Material {
  id: number;
  name: string;
  description: string | null;
  createdAt: string;
  updatedAt: string;
  files: Array<MaterialFile>;
}

export interface MaterialFile {
  id: number;
  name: string;
  path: string;
  mimeType: string;
}

export const MaterialCreateSchema = z.object({
	name: z.string().min(1, "Name is required").max(64, "Name should be less than 64 characters"),
	file: z
		.instanceof(FileList, {
			message: "File is required",
		})
		.refine(
			(value) => value.length === 1 && value[0].size < 5 * 1024 * 1024,
			"File size should be less than 5MB",
		),
	materialGroupId: z.coerce.number(),
	description: z.string().max(512, "Description should be less than 512 characters").optional(),
});

export type MaterialCreateData = z.infer<typeof MaterialCreateSchema>
