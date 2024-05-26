import { Course, CourseCreateData, CourseEditData } from "@/types/Course";
import { createGenericObjectService } from "./GenericObject.service";

export const CourseService = createGenericObjectService<
  Course,
  CourseCreateData,
  CourseEditData & { id: number }
>({
  entityName: "course",
  entityEndpoint: "course",
});
