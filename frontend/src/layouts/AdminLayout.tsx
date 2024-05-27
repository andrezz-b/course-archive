import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { Shield } from "lucide-react";
import { Outlet, NavLink } from "react-router-dom";

const AdminLayout = () => {
  return (
    <div className="flex justify-center  flex-1">
      <div className="flex flex-1 max-w-[75vw] border-accent">
        <aside className="w-64 flex flex-col p-4 border-accent border-r-4">
          <h2 className="border-none text-xl flex items-center">
            <Shield className="w-5 h-5 mt-1 mr-2" />
            Course Admin
          </h2>
          <nav className="flex flex-col gap-1">
            <NavLink
              to="/admin/college"
              className={({ isActive }) => cn({ "bg-muted": isActive }, "w-full rounded-md")}
            >
              <Button variant="ghost" className="w-full justify-start">
                Colleges
              </Button>
            </NavLink>
            <NavLink
              to="/admin/program"
              className={({ isActive }) => cn({ "bg-muted": isActive }, "w-full rounded-md")}
            >
              <Button variant="ghost" className="w-full justify-start">
                Programs
              </Button>
            </NavLink>
            <NavLink
              to="/admin/course"
              className={({ isActive }) => cn({ "bg-muted": isActive }, "w-full rounded-md")}
            >
              <Button variant="ghost" className="w-full justify-start">
                Courses
              </Button>
            </NavLink>
          </nav>
        </aside>
        <main className="flex-1 p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default AdminLayout;
