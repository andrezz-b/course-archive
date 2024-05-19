import AuthService from "@/api/auth.service";
import useAuth from "./useAuth";

const useRefreshToken = () => {
  const { setAuth } = useAuth();
  const { mutateAsync: refreshToken } = AuthService.useRefreshToken({
    onSuccess: (data) => {
      setAuth((prev) => ({
        ...prev!,
        accessToken: data.accessToken,
      }));
    },
  });
  return refreshToken;
};

export default useRefreshToken;
