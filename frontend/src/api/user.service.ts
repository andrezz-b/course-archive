import useAuth from "@/hooks/useAuth";
import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import {GetObjectPermissionData, ObjectPermission, User} from "@/types/User";
import { useQuery, UseQueryResult } from "@tanstack/react-query";
import {createGenericObjectService, GenericObjectService} from "@/api/GenericObject.service.ts";
import { ApiError } from "@/api/config/ApiError.ts";

type UserService = Pick<
  GenericObjectService<User, unknown, unknown>,
  "useGetAll" | "useGetById"
> & {
  useGetCurrentUser: () => UseQueryResult<User, ApiError>;
  useGetObjectPermissions: (data: GetObjectPermissionData) => UseQueryResult<ObjectPermission, ApiError>
};
const genericUserService = createGenericObjectService<User, unknown, unknown & {id: number}>({
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
  useGetAll: genericUserService.useGetAll,
  useGetById: genericUserService.useGetById,
};
