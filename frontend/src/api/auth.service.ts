import type { User } from "@/types/User";
import type { LoginData, LoginResponse, RefreshResponse, RegisterData } from "@/types/Auth";
import { axiosPublic } from "./config/axios";
import { UseMutationOptions, useMutation, useQueryClient } from "@tanstack/react-query";
import useLocalStorage from "@/hooks/useLocalStorage";
import useAuth from "@/hooks/useAuth";
import { ApiError } from "./config/ApiError";

const AuthService = {
  useLogin: <T extends LoginResponse, Err extends ApiError>(
    mutationOptions?: Omit<UseMutationOptions<T, Err, LoginData>, "mutationFn" | "onSuccess">,
  ) => {
    const [, setRefreshToken] = useLocalStorage<string>("refreshToken", "");
    const { setAuth } = useAuth();
    const queryClient = useQueryClient();
    return useMutation<T, Err, LoginData>({
      mutationFn: async (loginData) => {
        try {
          const response = await axiosPublic.post<T>("/auth/login", loginData, {
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

  useRefreshToken: <Data extends RefreshResponse, Err extends ApiError>(
    mutationOptions?: Omit<UseMutationOptions<Data, Err>, "mutationFn">,
  ) => {
    // When in StrictMode useLocalStorage sometimes returns the default value so this is a workaround
    const localStorageRefreshToken = localStorage.getItem("refreshToken");
    const refreshToken = localStorageRefreshToken ? JSON.parse(localStorageRefreshToken) : "";
    return useMutation<Data, Err>({
      mutationFn: async () => {
        try {
          const response = await axiosPublic.post<Data>(
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

  useRegister: <Data extends User, Err extends ApiError>(
    mutationOptions?: Omit<UseMutationOptions<Data, Err, RegisterData>, "mutationFn">,
  ) => {
    return useMutation({
      mutationFn: async (registerData) => {
        try {
          const response = await axiosPublic.post<Data>("/auth/register", registerData);
          return response.data;
        } catch (e) {
          throw new ApiError(e);
        }
      },
      ...mutationOptions,
    });
  },

  useLogout: <T = void>(mutationOptions?: Omit<UseMutationOptions<T>, "mutationFn">) => {
    return useMutation({
      mutationFn: async () => {
        const response = await axiosPublic.get<T>("/auth/logout", {
          withCredentials: true,
        });
        return response.data;
      },
      ...mutationOptions,
    });
  },
};

export default AuthService;
