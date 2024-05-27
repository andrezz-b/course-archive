import {
  MaterialGroup,
  MaterialGroupCreateData,
  MaterialGroupEditData,
} from "@/types/MaterialGroup";
import { createGenericObjectService } from "./GenericObject.service";

export const MaterialGroupService = createGenericObjectService<
  MaterialGroup,
  MaterialGroupCreateData,
  MaterialGroupEditData & { id: number }
>({
  entityName: "material-group",
  entityEndpoint: "material/group",
});
