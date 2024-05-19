import Navbar from "@/components/Navbar";
import useAuth from "@/hooks/useAuth";
import { Navigate, Outlet } from "react-router-dom";

const RootLayout = () => {
  const {auth} = useAuth();

  if (auth?.accessToken) {
    return <Navigate to="/" />;
  }
  
  return (
    <>
      <Navbar />
      <Outlet />
    </>
  );
};

export default RootLayout;
