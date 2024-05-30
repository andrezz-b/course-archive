import useAuth from "@/hooks/useAuth";
import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import { User } from "@/types/User";
import { useQuery, UseQueryResult } from "@tanstack/react-query";
import { GenericObjectService } from "@/api/GenericObject.service.ts";
import { ApiError } from "@/api/config/ApiError.ts";
import {useMemo} from "react";
import {getDefinedValuesObject} from "@/lib/utils.ts";
import {DISPLAY_LISTING_PAGE_SIZE} from "@/types/Common.ts";

type UserService = Pick<GenericObjectService<User, unknown, unknown>, "useGetAll"> & {
  useGetCurrentUser: () => UseQueryResult<User, ApiError>;
};

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

  useGetAll: (params, options) => {
    const axios = useAxiosPrivate();
    const definedParams = useMemo(() => {
      if (!params) return {};
      const definedValues = getDefinedValuesObject(params);
      definedValues.size = params.size || DISPLAY_LISTING_PAGE_SIZE;
      return definedValues;
    }, [params]);

    return useQuery({
      queryKey: ["user", "all", definedParams],
      queryFn: async () => {
        try {
          const { data } = await axios.get(`/user/`, {
            params: definedParams,
            paramsSerializer: {
              indexes: null
            }
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
