import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import { ApiError } from "./config/ApiError";
import { College, CollegeFilterField, CollegeSortField } from "@/types/College";
import {
  DefinedInitialDataInfiniteOptions,
  DefinedInitialDataOptions,
  InfiniteData,
  useInfiniteQuery,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import { Page } from "@/types/Page";
import { SortDirection } from "@/types/Common";
import { useMemo } from "react";
import { getDefinedValuesObject } from "@/lib/utils";

interface GetAllColegesParams {
  sortField?: CollegeSortField;
  sortDirection?: SortDirection;
  filterField?: CollegeFilterField;
  filterValue?: string;
  size?: number;
}
const COLLEGE_PAGE_SIZE = 6;

export const CollegeService = {
  useGetAllColleges: <Data extends Page<College>, Err extends ApiError>(
    params?: GetAllColegesParams,
    options?: Omit<
      DefinedInitialDataInfiniteOptions<Data, Err>,
      "getNextPageParam" | "queryKey" | "queryFn" | "initialPageParam"
    >,
  ) => {
    const axios = useAxiosPrivate();
    const definedParams = useMemo(() => {
      if (!params) return {};
      const definedValues = getDefinedValuesObject(params);
      definedValues.size = params.size || COLLEGE_PAGE_SIZE;
      return definedValues;
    }, [params]);

    return useInfiniteQuery<Data, Err>({
      getNextPageParam: (lastPage) => (!lastPage.last ? lastPage.number + 1 : null),
      queryKey: ["college", "all", definedParams],
      queryFn: async ({ pageParam = 0 }) => {
        try {
          const { data } = await axios.get<Data>(`/college/`, {
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
  },
  useGetCollegeById: <Data extends College, Err extends ApiError>(
    id?: number,
    options?: DefinedInitialDataOptions<Data, Err>,
  ) => {
    const axios = useAxiosPrivate();
    const queryClient = useQueryClient();
    return useQuery<Data, Err>({
      queryKey: ["college", id],
      queryFn: async () => {
        try {
          const { data } = await axios.get<Data>(`/college/${id}`);
          return data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      enabled: !!id,
      staleTime: 60e3,
      initialData: () => {
        const values = queryClient.getQueriesData<InfiniteData<Page<Data>>>({
          stale: false,
          queryKey: ["college", "all"],
        });

        for (const [, data] of values) {
          if (data?.pages) {
            for (const page of data.pages) {
              const college = page.content.find((college) => college.id === id);
              if (college !== undefined) return college;
            }
          }
        }
        return undefined;
      },
      ...options,
    });
  },
};
