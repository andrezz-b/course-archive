import { Program, ProgramFilterField, ProgramSortField } from "@/types/Program";
import { ApiError } from "./config/ApiError";
import { Page } from "@/types/Page";
import { UndefinedInitialDataOptions, useQuery } from "@tanstack/react-query";
import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import { useMemo } from "react";
import { getDefinedValuesObject } from "@/lib/utils";
import { DISPLAY_LISTING_PAGE_SIZE, SortDirection } from "@/types/Common";

interface GetAllProgramsParams {
  sortField?: ProgramSortField;
  sortDirection?: SortDirection;
  filterField?: ProgramFilterField;
  filterValue?: string;
  size?: number;
  collegeId?: number;
}

export const ProgramService = {
  useGetPrograms: <Data extends Page<Program>, Err extends ApiError>(
    params?: GetAllProgramsParams,
    options?: Omit<UndefinedInitialDataOptions<Data, Err>, "queryKey" | "queryFn" | "staleTime">,
  ) => {
    const axios = useAxiosPrivate();
    const definedParams = useMemo(() => {
      if (!params) return {};
      const definedValues = getDefinedValuesObject(params);
      definedValues.size = params.size || DISPLAY_LISTING_PAGE_SIZE;
      return definedValues;
    }, [params]);

    return useQuery<Data, Err>({
      queryKey: ["college", "all", definedParams],
      queryFn: async () => {
        try {
          const { data } = await axios.get<Data>("/program/", {
            params: definedParams,
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
}