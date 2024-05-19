import Loading from "@/components/Loading";
import CurrentUserProvider from "@/context/CurrentUserProvider";
import useAuth from "@/hooks/useAuth";
import useRefreshToken from "@/hooks/useRefreshToken";
import React, { useState, useEffect } from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";

interface ProtectedRoutesProps {
  layout?: React.ReactNode;
}

const ProtectedRoutes = ({ layout = <Outlet /> }: ProtectedRoutesProps) => {
  const [isLoading, setIsLoading] = useState(true);
  const refresh = useRefreshToken();
  const location = useLocation();
  const { auth } = useAuth();

  useEffect(() => {
    let isMounted = true;

    const verifyRefreshToken = async () => {
      try {
        await refresh();
      } catch (err) {
        console.warn("No session found or session expired, redirecting to login.");
        console.error((err as Error).message);
      } finally {
        isMounted && setIsLoading(false);
      }
    };

    !auth?.accessToken ? verifyRefreshToken() : setIsLoading(false);

    return () => {
      isMounted = false;
    };
  }, [auth?.accessToken, refresh]);

  // TODO: Add loading spinner
  if (isLoading) return <Loading />;

  if (!auth?.accessToken) return <Navigate to="/login" state={{ from: location }} replace />;

  return <CurrentUserProvider>{layout}</CurrentUserProvider>;
};

export default ProtectedRoutes;
