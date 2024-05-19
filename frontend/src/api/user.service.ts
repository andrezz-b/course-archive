import useAuth from "@/hooks/useAuth";
import useAxiosPrivate from "@/hooks/useAxiosPrivate";
import { User } from "@/types/User";
import { useQuery } from "@tanstack/react-query";

export const UserService = {
  useGetCurrentUser() {
    const { auth } = useAuth();
    const axios = useAxiosPrivate();
    return useQuery<User, Error>({
      queryKey: ["currentUser", auth?.accessToken],
      queryFn: () => axios.get("/user/current").then((response) => response.data),
      staleTime: Infinity,
    });
  },
};