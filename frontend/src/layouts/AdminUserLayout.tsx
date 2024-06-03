import { NavLink, Outlet, useNavigate, useParams } from "react-router-dom";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import { Button } from "@/components/ui/button.tsx";
import { ChevronLeft } from "lucide-react";
import { UserService } from "@/api/user.service.ts";
import { Separator } from "@/components/ui/separator.tsx";
import { cn } from "@/lib/utils.ts";

const AdminUserLayout = () => {
  const navigate = useNavigate();
  const { userId } = useParams<{ userId: string; courseId: string }>();
  const userQuery = UserService.useGetById(userId ? parseInt(userId) : undefined);

  return (
    <div>
      {userQuery.isLoading ? (
        <div className="flex gap-2 my-2">
          <Skeleton className="w-[40px] h-[35px]" />
          <Skeleton className="w-[400px] h-[35px]" />
        </div>
      ) : (
        <div className="flex items-center mb-4 gap-4 min-h-[60px]">
          <Button
            variant="outline"
            className="w-8 h-8 p-0 flex-shrink-0"
            onClick={() => navigate(-1)}
          >
            <ChevronLeft />
          </Button>
          <h3 className="flex items-center gap-4 text-xl md:text-3xl">
            {userQuery.data?.username}
          </h3>
        </div>
      )}
      <div className="container space-y-4">
        <ul className="flex gap-5 h-8 items-center mb-2">
          <NavLink
            to={`/admin/user/${userId}/college`}
            className={({ isActive }) =>
              cn("py-1 px-3 border-b-2 border-transparent", { "border-primary": isActive })
            }
          >
            Colleges
          </NavLink>
          <Separator orientation="vertical" />
          <NavLink
            to={`/admin/user/${userId}/program`}
            className={({ isActive }) =>
              cn("py-1 px-3 border-b-2 border-transparent", { "border-primary": isActive })
            }
          >
            Programs
          </NavLink>
          <Separator orientation="vertical" />
          <NavLink
            to={`/admin/user/${userId}/course`}
            className={({ isActive }) =>
              cn("py-1 px-3 border-b-2 border-transparent", { "border-primary": isActive })
            }
          >
            Courses
          </NavLink>
        </ul>
        <Outlet />
      </div>
    </div>
  );
};

export default AdminUserLayout;
