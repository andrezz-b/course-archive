import { CurrentUserContext } from "@/context/CurrentUserProvider";
import { useContext } from "react";

const useCurrentUserContext = () => {
  const currentUser = useContext(CurrentUserContext);

  if (!currentUser) {
    throw new Error("CurrentUserContext: No value provided");
  }

  return currentUser;
};

export default useCurrentUserContext;
