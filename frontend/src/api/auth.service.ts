import type { User } from "@/types/User";
import type {
  LoginData,
  LoginResponse,
  RefreshResponse,
  RegisterData,
} from "@/types/Auth";
import { axiosPublic } from "./config/axios";
import { UseMutationOptions, useMutation, useQueryClient } from "@tanstack/react-query";
import { AxiosError } from "axios";
import useLocalStorage from "@/hooks/useLocalStorage";
import useAuth from "@/hooks/useAuth";

const AuthService = {
  useLogin: <T extends LoginResponse>(
    mutationOptions?: Omit<
      UseMutationOptions<T, Error, LoginData>,
      "mutationFn" | "onSuccess"
    >,
  ) => {
    const [, setRefreshToken] = useLocalStorage<string>("refreshToken", "");
    const { setAuth } = useAuth();
    const queryClient = useQueryClient();
    return useMutation<T, Error, LoginData>({
      mutationFn: async ({ email, password }) => {
        const response = await axiosPublic.post<T>(
          "/auth/login",
          { email, password },
          { withCredentials: true },
        );
        return response.data;
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

  useRefreshToken: <T extends RefreshResponse>(
    mutationOptions?: Omit<UseMutationOptions<T>, "mutationFn">,
  ) => {
    // When in StrictMode useLocalStorage sometimes returns the default value so this is a workaround
    const localStorageRefreshToken = localStorage.getItem("refreshToken");
    const refreshToken = localStorageRefreshToken ? JSON.parse(localStorageRefreshToken) : "";
    return useMutation({
      mutationFn: async () => {
        try {
          const response = await axiosPublic.post<T>(
            "/auth/refresh",
            { refreshToken },
            { withCredentials: true },
          );
          return response.data;
        } catch (e) {
          const error = e as AxiosError<Record<string, string>>;
          const message = `Status: ${error.response?.status}, Error: ${error.response?.data?.error}, Message: ${error.response?.data?.message}`;
          throw new Error(message);
        }
      },
      ...mutationOptions,
    });
  },

  useRegister: <T = User>(
    mutationOptions?: Omit<UseMutationOptions<T, Error, RegisterData>, "mutationFn">,
  ) => {
    return useMutation({
      mutationFn: async (registerData) => {
        const response = await axiosPublic.post<T>("/auth/register", registerData);
        return response.data;
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
