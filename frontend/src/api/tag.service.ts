import { createGenericObjectService } from "@/api/GenericObject.service.ts";
import { Tag, TagCreateData, TagEditData } from "@/types/Tag.ts";
import { UndefinedInitialDataOptions, UseQueryResult } from "@tanstack/react-query";
import { ApiError } from "@/api/config/ApiError.ts";

const GenericTagService = createGenericObjectService<
  Tag,
  TagCreateData,
  TagEditData & { id: number }
>({
  entityName: "tag",
  entityEndpoint: "material/tag",
});

export const TagService = {
  ...GenericTagService,
  useGetAll: GenericTagService.useGetAll as (
    params?: Record<string, unknown>,
    options?: Omit<UndefinedInitialDataOptions<Array<Tag>, ApiError>, "queryKey" | "queryFn">,
  ) => UseQueryResult<Array<Tag>, ApiError>,
};
