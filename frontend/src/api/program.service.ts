import { Program, ProgramFilterField, ProgramSortField } from "@/types/Program";
import { ApiError } from "./config/ApiError";
import { Page } from "@/types/Page";
import { DefinedInitialDataInfiniteOptions, useInfiniteQuery } from "@tanstack/react-query";
import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import { useMemo } from "react";
import { getDefinedValuesObject } from "@/lib/utils";
import { SortDirection } from "@/types/Common";

const PROGRAM_PAGE_SIZE = 6;

interface GetAllProgramsParams {
  sortField?: ProgramSortField;
  sortDirection?: SortDirection;
  filterField?: ProgramFilterField;
  filterValue?: string;
  size?: number;
  collegeId?: number;
}

export const ProgramService = {
  useGetAllPrograms: <Data extends Page<Program>, Err extends ApiError>(
    params?: GetAllProgramsParams,
    options?: Omit<
      DefinedInitialDataInfiniteOptions<Data, Err>,
      "getNextPageParam" | "queryKey" | "queryFn" | "initialPageParam"
    >,
  ) => {
    const axios = useAxiosPrivate();
    const definedParams = useMemo(() => {
      if (!params) return {};
      const definedValues = getDefinedValuesObject(params);
      definedValues.size = params.size || PROGRAM_PAGE_SIZE;
      return definedValues;
    }, [params]);

    return useInfiniteQuery<Data, Err>({
      getNextPageParam: (lastPage) => (!lastPage.last ? lastPage.number + 1 : null),
      queryKey: ["program", "all", definedParams],
      queryFn: async ({ pageParam = 0 }) => {
        try {
          const { data } = await axios.get<Data>(`/program/`, {
            params: {
              ...definedParams,
              page: pageParam,
            },
          });
          return data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      initialPageParam: 0,
      staleTime: 60e3,
      ...options,
    });
  }
}