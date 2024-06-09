import { z } from "zod";
import {Tag} from "@/types/Tag.ts";

export interface Material {
	id: number;
	name: string;
	description: string | null;
	files: Array<MaterialFile>;
  tags: Array<Tag>;
  voteCount: number;
  currentUserVote: MaterialVote | null;
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
	materialGroupId: z.coerce.number().min(1, "Material group is required"),
	description: z.string().max(512, "Description should be less than 512 characters").optional(),
  tagIds: z.array(z.coerce.number()).optional(),
});

export type MaterialCreateData = z.infer<typeof MaterialCreateSchema>;

export const MaterialEditSchema = MaterialCreateSchema.omit({ file: true });

export type MaterialEditData = z.infer<typeof MaterialEditSchema>;

export enum MaterialVote {
  DOWNVOTE = "DOWNVOTE",
  UPVOTE = "UPVOTE",
}

export interface MaterialVoteData {
  voteType: MaterialVote;
  materialId: number;
}
