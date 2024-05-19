import { useNavigate } from "react-router-dom";
import useAuth from "./useAuth";
import useLocalStorage from "./useLocalStorage";
import { useQueryClient } from "@tanstack/react-query";

const useLogout = () => {
  const { setAuth } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [, setRefreshToken] = useLocalStorage("refreshToken", "");
  const logout = () => {
    setAuth(null);
    setRefreshToken("");
    queryClient.clear();
    navigate("/login");
  };

  return logout;
};

export default useLogout;
