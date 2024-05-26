import {
  Program,
  ProgramCreateData,
  ProgramEditData,
} from "@/types/Program";
import {  WithId } from "@/types/Common";
import { createGenericObjectService } from "./GenericObject.service";

export const ProgramService = createGenericObjectService<
  Program,
  ProgramCreateData,
  ProgramEditData & WithId
>({
  entityName: "program",
  entityEndpoint: "program",
});
