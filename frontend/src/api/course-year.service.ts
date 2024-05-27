import { CourseYear, CourseYearCreateData, CourseYearEditData } from "@/types/CourseYear";
import { createGenericObjectService } from "./GenericObject.service";

export const CourseYearService = createGenericObjectService<
  CourseYear,
  CourseYearCreateData,
  CourseYearEditData & { id: number }
>({
  entityName: "course-year",
  entityEndpoint: "course/year",
});
