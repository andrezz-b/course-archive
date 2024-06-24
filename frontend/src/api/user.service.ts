import useAuth from "@/hooks/useAuth";
import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import { GetObjectPermissionData, ObjectPermission, User } from "@/types/User";
import {
  useMutation,
  UseMutationOptions,
  UseMutationResult,
  useQuery,
  useQueryClient,
  UseQueryResult,
} from "@tanstack/react-query";
import { createGenericObjectService, GenericObjectService } from "@/api/GenericObject.service.ts";
import { ApiError } from "@/api/config/ApiError.ts";
import { WithId } from "@/types/Common.ts";

type UserService = Pick<
  GenericObjectService<User, unknown, unknown>,
  "useGetAll" | "useGetById"
> & {
  useGetCurrentUser: () => UseQueryResult<User, ApiError>;
  useGetObjectPermissions: (
    data: GetObjectPermissionData,
  ) => UseQueryResult<ObjectPermission, ApiError>;
  useAddCourseToFavorite: (
    mutationOptions?: Omit<UseMutationOptions<undefined, ApiError, WithId>, "mutationFn" | "onSuccess">,
  ) => UseMutationResult<undefined, ApiError, WithId>;
  useRemoveCourseFromFavorite: (
    mutationOptions?: Omit<UseMutationOptions<undefined, ApiError, WithId>, "mutationFn" | "onSuccess">,
  ) => UseMutationResult<undefined, ApiError, WithId>;
};

const genericUserService = createGenericObjectService<User, unknown, unknown & { id: number }>({
  entityName: "user",
  entityEndpoint: "user",
});
export const UserService: UserService = {
  useGetCurrentUser() {
    const { auth } = useAuth();
    const axios = useAxiosPrivate();
    return useQuery({
      queryKey: ["currentUser", auth?.accessToken],
      queryFn: async () => {
        try {
          const { data } = await axios.get("/user/current");
          return data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      staleTime: Infinity,
    });
  },
  useGetObjectPermissions: (data) => {
    const axios = useAxiosPrivate();
    return useQuery({
      queryKey: ["user", "permission", data],
      queryFn: async () => {
        try {
          const response = await axios.post<ObjectPermission>("/user/object/permissions", data);
          return response.data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      staleTime: 60e3,
    });
  },
  useAddCourseToFavorite: (mutationOptions) => {
    const queryClient = useQueryClient();
    const axios = useAxiosPrivate();
    return useMutation({
      mutationFn: async ({ id }) => {
        try {
          const { data } = await axios.post(`/user/course/favorite/${id}`);
          return data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["course", "favorites"],
        });
      },
      ...mutationOptions
    });
  },
  useRemoveCourseFromFavorite: (mutationOptions) => {
    const queryClient = useQueryClient();
    const axios = useAxiosPrivate();
    return useMutation({
      mutationFn: async ({ id }) => {
        try {
          const { data } = await axios.delete(`/user/course/favorite/${id}`);
          return data;
        } catch (error) {
          throw new ApiError(error);
        }
      },
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["course", "favorites"],
        });
      },
      ...mutationOptions
    });
  },
  useGetAll: genericUserService.useGetAll,
  useGetById: genericUserService.useGetById,
};
