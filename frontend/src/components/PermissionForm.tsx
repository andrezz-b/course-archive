import { UserService } from "@/api/user.service.ts";
import { ObjectType } from "@/types/Common.ts";
import { Permission, PermissionName } from "@/types/Auth.ts";
import { Button } from "@/components/ui/button.tsx";
import { useMemo } from "react";
import { cn } from "@/lib/utils.ts";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip.tsx";
import { Skeleton } from "@/components/ui/skeleton.tsx";

const PermissionForm = () => {
  const query = UserService.useGetObjectPermissions({
    objectType: ObjectType.PROGRAM,
    objectId: 1,
    username: "test-user-3",
  });

  // const handleSubmit = (data: PermissionName) => {
  //   console.log(data);
  // };

  const permissions = useMemo(() => {
    if (!query.data) return [];
    return Object.values(Permission).map((permissionName) => ({
      permission: permissionName,
      granted: query.data[permissionName].granted,
      grantedByParent: query.data[permissionName].grantedByParent,
      grantedByHigherPermission: query.data[permissionName].grantedByHigherPermission,
    }));
  }, [query.data]);

  return (
    <div>
      <h4 className="mb">Permission form</h4>
      <span className="text-muted-foreground text-sm">
        Hover the permission buttons to see tooltip with explanation.
      </span>
      <div className="flex items-center gap-4 mt-4">
        <TooltipProvider delayDuration={700}>
          {query?.data
            ? permissions.map((permission) => (
                <PermissionButton key={permission.permission} {...permission} />
              ))
            : Array.from({ length: Object.keys(Permission).length }).map((_, index) => (
                <Skeleton key={index} className="w-20 h-10" />
              ))}
        </TooltipProvider>
      </div>
    </div>
  );
};

const getTooltipContent = (
  granted: boolean,
  grantedParent: boolean,
  grantedHigherPermission: boolean,
) => {
  if (granted && !grantedParent && !grantedHigherPermission) {
    return "Granted on this object. Click to revoke.";
  }

  if (grantedParent && !grantedHigherPermission) {
    return "Granted by parent object. Revoke on the parent.";
  }

  if (!grantedParent && grantedHigherPermission) {
    return "Granted by higher permission. Revoke by revoking the higher permission. Granting will have no effect.";
  }

  if (grantedParent && grantedHigherPermission) {
    return "Granted by parent's higher permission. Revoke higher permission on the parent.";
  }

  return "Not granted. Click to grant.";
};

interface PermissionButtonProps {
  permission: PermissionName;
  granted: boolean;
  grantedByParent: boolean;
  grantedByHigherPermission: boolean;
}

const PermissionButton = ({
  permission,
  granted,
  grantedByParent,
  grantedByHigherPermission,
}: PermissionButtonProps) => {
  const content = useMemo(
    () => getTooltipContent(granted, grantedByParent, grantedByHigherPermission),
    [granted, grantedByParent, grantedByHigherPermission],
  );
  return (
    <Tooltip>
      <TooltipTrigger asChild>
        <Button
          className={cn("text-foreground hover:text-background border border-transparent", {
            "bg-success": granted,
            "bg-success opacity-50 cursor-not-allowed": grantedByParent,
            "bg-muted": !granted && !grantedByHigherPermission,
            "border-success border bg-transparent": grantedByHigherPermission,
          })}
        >
          {permission}
        </Button>
      </TooltipTrigger>
      <TooltipContent className="max-w-[300px]">{content}</TooltipContent>
    </Tooltip>
  );
};

export default PermissionForm;
