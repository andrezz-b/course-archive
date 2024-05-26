import { College, CollegeCreateData, CollegeEditData } from "@/types/College";
import { createGenericObjectService } from "./GenericObject.service";

export const CollegeService = createGenericObjectService<
  College,
  CollegeCreateData,
  CollegeEditData & { id: number }
>({
  entityName: "college",
  entityEndpoint: "college",
});
