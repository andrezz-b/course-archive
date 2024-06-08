import { createGenericObjectService } from "@/api/GenericObject.service.ts";
import { Tag, TagCreateData, TagEditData } from "@/types/Tag.ts";

export const TagService = createGenericObjectService<
  Tag,
  TagCreateData,
  TagEditData & { id: number }
>({
  entityName: "tag",
  entityEndpoint: "material/tag",
});
