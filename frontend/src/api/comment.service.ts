import { createGenericObjectService } from "@/api/GenericObject.service.ts";
import { CommentCreateData, CommentEditData, MaterialComment } from "@/types/MaterialComment.ts";

export const CommentService = createGenericObjectService<
  MaterialComment,
  CommentCreateData,
  CommentEditData & { id: number }
>({
  entityName: "comment",
  entityEndpoint: "material/comment",
});
