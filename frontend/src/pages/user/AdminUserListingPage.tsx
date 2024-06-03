import { ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import { Button } from "@/components/ui/button.tsx";
import { ExternalLink } from "lucide-react";
import { User } from "@/types/User";
import { Link } from "react-router-dom";
import { DataTable, DataTableColumnHeader } from "@/components/ui/data-table.tsx";
import { UserService } from "@/api/user.service.ts";
import { keepPreviousData } from "@tanstack/react-query";
import { useTableControls } from "@/hooks/useTableControls.ts";

const AdminUserListingPage = () => {
  const { requestParams, ...tableProps } = useTableControls();
  const query = UserService.useGetAll(requestParams, {
    placeholderData: keepPreviousData,
  });

  const defaultData = useMemo(() => [], []);
  const columns: ColumnDef<User>[] = useMemo(
    () => [
      {
        header: ({ column }) => <DataTableColumnHeader column={column} title="ID" />,
        accessorKey: "id",
        enableSorting: false,
        enableColumnFilter: false,
      },
      {
        header: ({ column }) => <DataTableColumnHeader column={column} title="Username" />,
        accessorKey: "username",
      },
      {
        header: ({ column }) => <DataTableColumnHeader column={column} title="Email" />,
        accessorKey: "email",
      },
      {
        header: ({ column }) => <DataTableColumnHeader column={column} title="First name" />,
        accessorKey: "firstName",
      },
      {
        header: ({ column }) => <DataTableColumnHeader column={column} title="Last name" />,
        accessorKey: "lastName",
      },
      {
        id: "actions",
        header: "Actions",
        cell: ({ row }) => {
          return (
            <Link to={`./${row.original.id}/college`}>
              <Button variant="ghost" className="p-1 h-auto">
                <span className="sr-only">edit college</span>
                <ExternalLink className="h-4 w-4" />
              </Button>
            </Link>
          );
        },
      },
    ],
    [],
  );

  return (
    <div className="container">
      <h3 className="mb-5">Users</h3>
      <DataTable
        columns={columns}
        data={query.data?.content ?? defaultData}
        totalElements={query.data?.totalElements ?? -1}
        {...tableProps}
      />
    </div>
  );
};

export default AdminUserListingPage;
