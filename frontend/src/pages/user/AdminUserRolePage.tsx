import { useParams } from "react-router-dom";
import { UserService } from "@/api/user.service.ts";
import { Button } from "@/components/ui/button.tsx";
import { Role } from "@/types/Auth.ts";
import { cn } from "@/lib/utils.ts";
import { useMemo } from "react";
import AuthService from "@/api/auth.service.ts";
import { toast } from "sonner";

const AdminUserRolePage = () => {
  const { userId } = useParams<{ userId: string }>();
  const userQuery = UserService.useGetById(userId ? parseInt(userId) : undefined);
  const { mutate: updateRole } = AuthService.useChangeRole();

  const [isManager, isAdmin] = useMemo(() => {
    const adminRole = !!userQuery.data?.roles.includes(Role.ADMIN);
    const managerRole = !!userQuery.data?.roles.includes(Role.MANAGER);
    return [managerRole, adminRole];
  }, [userQuery.data?.roles]);

  const handleUpdateRole = (role: Role, granting: boolean) => {
    if (!userQuery.data) return;
    updateRole(
      {
        userId: parseInt(userId!),
        username: userQuery.data.username,
        role,
        granting,
      },
      {
        onError: (error) => toast.error(error.getErrorMessage()),
      },
    );
  };
  return (
    <div className="flex gap-4">
      <Button className="text-success-foreground bg-success" disabled>
        {Role.USER}
      </Button>
      <Button
        onClick={() => handleUpdateRole(Role.MANAGER, !isManager)}
        className={cn("text-foreground hover:text-background border border-transparent bg-muted", {
          "border-success border bg-transparent text-foreground": isAdmin,
          "bg-success text-success-foreground": isManager,
        })}
      >
        {Role.MANAGER}
      </Button>
      <Button
        onClick={() => handleUpdateRole(Role.ADMIN, !isAdmin)}
        className={cn("text-foreground hover:text-background border border-transparent bg-muted", {
          "bg-success text-success-foreground": isAdmin,
        })}
      >
        {Role.ADMIN}
      </Button>
    </div>
  );
};

export default AdminUserRolePage;
