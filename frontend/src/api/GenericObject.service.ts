import { Page } from "@/types/Page";
import { ApiError } from "./config/ApiError";
import {
  DefinedInitialDataOptions,
  UndefinedInitialDataOptions,
  useMutation,
  UseMutationOptions,
  UseMutationResult,
  useQuery,
  useQueryClient,
  UseQueryResult,
} from "@tanstack/react-query";
import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import { useMemo } from "react";
import { getDefinedValuesObject } from "@/lib/utils";
import { DISPLAY_LISTING_PAGE_SIZE, WithId } from "@/types/Common";

interface GenericObjectServiceFactoryOptions {
  entityName: string;
  entityEndpoint?: string;
}

export interface GenericObjectService<Entity, CreateData, EditData> {
  useGetAll: (
    params?: Record<string, unknown>,
    options?: Omit<UndefinedInitialDataOptions<Page<Entity>, ApiError>, "queryKey" | "queryFn">,
  ) => UseQueryResult<Page<Entity>, ApiError>;

  useGetById: (
    id?: number,
    options?: Omit<DefinedInitialDataOptions<Entity, ApiError>, "queryKey" | "queryFn">,
  ) => UseQueryResult<Entity, ApiError>;

  useCreate: (
    mutationOptions?: Omit<UseMutationOptions<Entity, ApiError, CreateData>, "mutationFn">,
  ) => UseMutationResult<Entity, ApiError, CreateData>;

  useUpdateById: (
    mutationOptions?: Omit<UseMutationOptions<Entity, ApiError, EditData>, "mutationFn">,
  ) => UseMutationResult<Entity, ApiError, EditData>;

  useDeleteById: (
    mutationOptions?: Omit<UseMutationOptions<undefined, ApiError, WithId>, "mutationFn">,
  ) => UseMutationResult<undefined, ApiError, WithId>;
}

export function createGenericObjectService<
  Entity extends WithId,
  EntityCreateData,
  EntityEditData extends WithId,
>({
  entityName,
  entityEndpoint = entityName,
}: GenericObjectServiceFactoryOptions): GenericObjectService<
  Entity,
  EntityCreateData,
  EntityEditData
> {
  return {
    useGetAll: (params, options) => {
      const axios = useAxiosPrivate();
      const definedParams = useMemo(() => {
        if (!params) return {};
        const definedValues = getDefinedValuesObject(params);
        definedValues.size = params.size || DISPLAY_LISTING_PAGE_SIZE;
        return definedValues;
      }, [params]);

      return useQuery({
        queryKey: [entityName, "all", definedParams],
        queryFn: async () => {
          try {
            const { data } = await axios.get(`/${entityEndpoint}/`, {
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

    useGetById: (id, options) => {
      const axios = useAxiosPrivate();
      const queryClient = useQueryClient();

      return useQuery({
        queryKey: [entityName, id],
        queryFn: async () => {
          try {
            const { data } = await axios.get<Entity>(`/${entityEndpoint}/${id}`);
            return data;
          } catch (error) {
            throw new ApiError(error);
          }
        },
        enabled: options?.enabled ?? !!id,
        staleTime: options?.staleTime ?? 60e3,
        initialData: () => {
          const values = queryClient.getQueriesData<Page<Entity>>({
            stale: false,
            queryKey: [entityName, "all"],
          });

          for (const [, data] of values) {
            const entity = data?.content.find((item) => item.id === id);
            if (entity) {
              return entity;
            }
          }
          return undefined;
        },
        ...options,
      });
    },

    useCreate: (mutationOptions) => {
      const axios = useAxiosPrivate();
      const queryClient = useQueryClient();

      return useMutation({
        mutationFn: async (body) => {
          try {
            const { data } = await axios.post<Entity>(`/${entityEndpoint}/`, body);
            return data;
          } catch (error) {
            throw new ApiError(error);
          }
        },
        onSuccess: () => {
          queryClient.invalidateQueries({
            queryKey: [entityName],
          });
        },
        ...mutationOptions,
      });
    },

    useUpdateById: (mutationOptions) => {
      const axios = useAxiosPrivate();
      const queryClient = useQueryClient();

      return useMutation({
        mutationFn: async (data) => {
          const { id, ...rest } = data;
          try {
            const { data } = await axios.put<Entity>(`/${entityEndpoint}/${id}`, rest);
            return data;
          } catch (error) {
            throw new ApiError(error);
          }
        },
        onSuccess: (data) => {
          queryClient.setQueriesData<Page<Entity>>(
            {
              queryKey: [entityName, "all"],
              stale: false,
            },
            (oldData) => {
              if (!oldData || !oldData?.content?.length) return oldData;
              return {
                ...oldData,
                content: oldData?.content.map((item) => {
                  if (item.id === data.id) return data;
                  return item;
                }),
              };
            },
          );
          queryClient.invalidateQueries({
            queryKey: [entityName],
          });
        },
        ...mutationOptions,
      });
    },
    useDeleteById: (mutationOptions) => {
      const axios = useAxiosPrivate();
      const queryClient = useQueryClient();

      return useMutation({
        mutationFn: async ({ id }) => {
          try {
            await axios.delete<void>(`/${entityEndpoint}/${id}`);
          } catch (error) {
            throw new ApiError(error);
          }
          return undefined;
        },
        onSuccess: () => {
          queryClient.invalidateQueries({
            queryKey: [entityName],
          });
        },
        ...mutationOptions,
      });
    },
  };
}
