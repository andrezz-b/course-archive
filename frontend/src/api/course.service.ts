import { Course, CourseCreateData, CourseEditData } from "@/types/Course";
import { createGenericObjectService, GenericObjectService } from "./GenericObject.service";
import { UndefinedInitialDataOptions, useQuery, UseQueryResult } from "@tanstack/react-query";
import { Page } from "@/types/Page";
import { ApiError } from "@/api/config/ApiError.ts";
import useAxiosPrivate from "@/hooks/useAxiosPrivate.ts";
import { useMemo } from "react";
import { getDefinedValuesObject } from "@/lib/utils.ts";
import { DISPLAY_LISTING_PAGE_SIZE } from "@/types/Common.ts";

interface ICourseService
  extends GenericObjectService<Course, CourseCreateData, CourseEditData & { id: number }> {
  useGetFavorites: (
    params?: Record<string, unknown>,
    options?: Omit<UndefinedInitialDataOptions<Page<Course>, ApiError>, "queryKey" | "queryFn">,
  ) => UseQueryResult<Page<Course>, ApiError>;
}

const genericCourseService = createGenericObjectService<
  Course,
  CourseCreateData,
  CourseEditData & { id: number }
>({
  entityName: "course",
  entityEndpoint: "course",
});
export const CourseService: ICourseService = {
  ...genericCourseService,
  useGetFavorites: (params, options) => {
    const axios = useAxiosPrivate();
    const definedParams = useMemo(() => {
      if (!params) return { size: DISPLAY_LISTING_PAGE_SIZE };
      const definedValues = getDefinedValuesObject(params);
      definedValues.size = params.size || DISPLAY_LISTING_PAGE_SIZE;
      return definedValues;
    }, [params]);

    return useQuery({
      queryKey: ["course", "favorites", definedParams],
      queryFn: async () => {
        try {
          const { data } = await axios.get(`/course/favorites`, {
            params: definedParams,
            paramsSerializer: {
              indexes: null,
            },
          });
          return data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      staleTime: 60e3,
      ...options,
    });
  },
};
