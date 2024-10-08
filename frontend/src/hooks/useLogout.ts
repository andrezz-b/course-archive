import {useNavigate} from "react-router-dom";
import useAuth from "./useAuth";
import useLocalStorage from "./useLocalStorage";
import {useQueryClient} from "@tanstack/react-query";

const useLogout = () => {
  const { setAuth } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [, setRefreshToken] = useLocalStorage("refreshToken", "");
  return () => {
    setAuth(null);
    setRefreshToken("");
    queryClient.clear();
    navigate("/login");
  };
};

export default useLogout;
