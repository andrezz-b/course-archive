import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import { ApiError } from "./config/ApiError";
import {
  College,
  CollegeCreateData,
  CollegeEditData,
  CollegeFilterField,
  CollegeSortField,
} from "@/types/College";
import {
  DefinedInitialDataInfiniteOptions,
  DefinedInitialDataOptions,
  InfiniteData,
  UndefinedInitialDataOptions,
  useInfiniteQuery,
  useMutation,
  UseMutationOptions,
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
  useGetColleges: <Data extends Page<College>, Err extends ApiError>(
    page: number,
    size = COLLEGE_PAGE_SIZE,
    options?: Omit<UndefinedInitialDataOptions<Data, Err>, "queryKey" | "queryFn" | "staleTime">,
  ) => {
    const axios = useAxiosPrivate();
    return useQuery<Data, Err>({
      queryKey: ["college", "admin", page],
      queryFn: async () => {
        try {
          const { data } = await axios.get<Data>(`/college/`, {
            params: {
              page,
              size,
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

  useCreateCollege: <
    Response extends College,
    Err extends ApiError,
    Data extends CollegeCreateData,
  >(
    mutationOptions?: Omit<UseMutationOptions<Response, Err, Data>, "mutationFn">,
  ) => {
    const axios = useAxiosPrivate();
    const queryClient = useQueryClient();
    return useMutation<Response, Err, Data>({
      mutationFn: async (body) => {
        try {
          const { data } = await axios.post<Response>("/college/", body);
          return data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["college"],
        });
      },
      ...mutationOptions,
    });
  },

  useUpdateCollegeById: <
    Response extends College,
    Err extends ApiError,
    Data extends Partial<CollegeEditData> & { id: number },
  >(
    mutationOptions?: Omit<UseMutationOptions<Response, Err, Data>, "mutationFn" | "onSuccess">,
  ) => {
    const axios = useAxiosPrivate();
    const queryClient = useQueryClient();

    return useMutation<Response, Err, Data>({
      mutationFn: async (data) => {
        const { id, ...rest } = data;
        const definedData = getDefinedValuesObject(rest);
        try {
          const { data } = await axios.put<Response>(`/college/${id}`, definedData);
          return data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      onSuccess: (data) => {
        queryClient.setQueriesData<Page<College>>(
          {
            queryKey: ["college", "admin"],
            stale: false,
          },
          (oldData) => {
            if (!oldData || !oldData?.content?.length) return oldData;
            return {
              ...oldData,
              content: oldData?.content.map((college) => {
                if (college.id === data.id) return data;
                return college;
              }),
            };
          },
        );
        queryClient.invalidateQueries({
          queryKey: ["college"],
        });
      },
      ...mutationOptions,
    });
  },
};
