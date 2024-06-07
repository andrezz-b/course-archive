import { z } from "zod";
import { SortDirection } from "@/types/Common.ts";

export interface MaterialComment {
  id: number;
  text: string;
  createdAt: string;
  updatedAt: string;
  edited: boolean;
  currentUser: boolean;
  username: string;
}

export enum CommentSortField {
  CREATED = "createdAt",
}

export const CommentSort = [
  {
    field: CommentSortField.CREATED,
    direction: SortDirection.DESC,
    label: "Newest",
    value: `${CommentSortField.CREATED}-${SortDirection.DESC}`,
  },
  {
    field: CommentSortField.CREATED,
    direction: SortDirection.ASC,
    label: "Oldest",
    value: `${CommentSortField.CREATED}-${SortDirection.ASC}`,
  },
];

export const CommentEditSchema = z.object({
  text: z.string().min(1).max(1024, "Text should be less than 1024 characters"),
});
export type CommentCreateData = z.infer<typeof CommentCreateSchema>;
export const CommentCreateSchema = CommentEditSchema.merge(
  z.object({
    materialId: z.number(),
  }),
);
export type CommentEditData = z.infer<typeof CommentEditSchema>;
