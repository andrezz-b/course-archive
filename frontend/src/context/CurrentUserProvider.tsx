import { createContext } from "react";
import type { User } from "@/types/User";
import { UserService } from "@/api/user.service";
export const CurrentUserContext = createContext<User | null>(null);

const CurrentUserProvider = ({ children }: { children: React.ReactNode }) => {
  const currentUserQuery = UserService.useGetCurrentUser();
  if (currentUserQuery.isLoading) {
    return null;
  }

  if (currentUserQuery.isError) {
    return <pre>{JSON.stringify(currentUserQuery.error)}</pre>;
  }

  return (
    <CurrentUserContext.Provider value={currentUserQuery.data ?? null}>
      {children}
    </CurrentUserContext.Provider>
  );
};

export default CurrentUserProvider;
