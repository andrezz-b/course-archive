import { Outlet } from "react-router-dom";

const HomeLayout = () => {
  return (
    <div className="flex flex-col flex-grow w-full py-4">
      <Outlet />
    </div>
  );
};

export default HomeLayout;
