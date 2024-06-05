import type { User } from "@/types/User";
import type {
  ChangePermissionData,
  LoginData,
  LoginResponse,
  RefreshResponse,
  RegisterData,
} from "@/types/Auth";
import { axiosPublic } from "./config/axios";
import {
  UseMutationOptions,
  useMutation,
  useQueryClient,
  UseMutationResult,
} from "@tanstack/react-query";
import useLocalStorage from "@/hooks/useLocalStorage";
import useAuth from "@/hooks/useAuth";
import { ApiError } from "./config/ApiError";
import useAxiosPrivate from "@/hooks/useAxiosPrivate.ts";

interface AuthService {
  useLogin: <T extends LoginResponse, Err extends ApiError>(
    mutationOptions?: Omit<UseMutationOptions<T, Err, LoginData>, "mutationFn" | "onSuccess">,
  ) => UseMutationResult<T, Err, LoginData>;

  useRefreshToken: <Data extends RefreshResponse, Err extends ApiError>(
    mutationOptions?: Omit<UseMutationOptions<Data, Err, void>, "mutationFn">,
  ) => UseMutationResult<Data, Err, void>;

  useRegister: <Data extends User, Err extends ApiError>(
    mutationOptions?: Omit<UseMutationOptions<Data, Err, RegisterData>, "mutationFn">,
  ) => UseMutationResult<Data, Err, RegisterData>;

  useLogout: (
    mutationOptions?: Omit<UseMutationOptions<unknown, ApiError, void>, "mutationFn">,
  ) => UseMutationResult<unknown, ApiError, void>;

  useChangePermission: <
    Data extends undefined,
    Err extends ApiError,
    Args extends ChangePermissionData,
  >(
    mutationOptions?: Omit<UseMutationOptions<Data, Err, Args>, "mutationFn">,
  ) => UseMutationResult<Data, Err, Args>;
}

const AuthService: AuthService = {
  useLogin: (mutationOptions) => {
    const [, setRefreshToken] = useLocalStorage<string>("refreshToken", "");
    const { setAuth } = useAuth();
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (loginData) => {
        try {
          const response = await axiosPublic.post("/auth/login", loginData, {
            withCredentials: true,
          });
          return response.data;
        } catch (e) {
          throw new ApiError(e);
        }
      },
      onSuccess: ({ accessToken, refreshToken }) => {
        queryClient.clear();
        setAuth({
          accessToken: accessToken,
        });
        setRefreshToken(refreshToken);
      },
      ...mutationOptions,
    });
  },

  useRefreshToken: (mutationOptions) => {
    // When in StrictMode useLocalStorage sometimes returns the default value so this is a workaround
    const localStorageRefreshToken = localStorage.getItem("refreshToken");
    const refreshToken = localStorageRefreshToken ? JSON.parse(localStorageRefreshToken) : "";
    return useMutation({
      mutationFn: async () => {
        try {
          const response = await axiosPublic.post(
            "/auth/refresh",
            { refreshToken },
            { withCredentials: true },
          );
          return response.data;
        } catch (e) {
          throw new ApiError(e);
        }
      },
      ...mutationOptions,
    });
  },

  useRegister: (mutationOptions) => {
    return useMutation({
      mutationFn: async (registerData) => {
        try {
          const response = await axiosPublic.post("/auth/register", registerData);
          return response.data;
        } catch (e) {
          throw new ApiError(e);
        }
      },
      ...mutationOptions,
    });
  },

  useLogout: (mutationOptions) => {
    return useMutation({
      mutationFn: async () => {
        const response = await axiosPublic.get("/auth/logout", {
          withCredentials: true,
        });
        return response.data;
      },
      ...mutationOptions,
    });
  },

  useChangePermission: (mutationOptions) => {
    const queryClient = useQueryClient();
    const axios = useAxiosPrivate();
    return useMutation({
      mutationFn: async (data) => {
        try {
          const response = await axios.post("/auth/permission", data);
          return response.data;
        } catch (e) {
          throw new ApiError(e);
        }
      },
      onSuccess: (_data, variables) => {
        queryClient.invalidateQueries({
          queryKey: [
            "user",
            "permission",
            {
              objectType: variables.objectType,
              objectId: variables.objectId,
              username: variables.username,
            },
          ],
        });
      },
      ...mutationOptions,
    });
  },
};

export default AuthService;
