import { College } from "@/types/College";
import { ColumnDef } from "@tanstack/react-table";
import { DataTable, DataTableColumnHeader } from "../../components/ui/data-table";
import { CollegeService } from "@/api/college.service";
import { useMemo } from "react";
import { keepPreviousData } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Pencil } from "lucide-react";
import { useTableControls } from "@/hooks/useTableControls.ts";

const AdminUserCollegePage = () => {
  const { requestParams, ...tableProps } = useTableControls();
  const query = CollegeService.useGetAll(
    requestParams,

    {
      placeholderData: keepPreviousData,
    },
  );

  const defaultData = useMemo(() => [], []);

  const columns: ColumnDef<College>[] = useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
      },
      {
        header: ({ column }) => <DataTableColumnHeader column={column} title="Name" />,
        accessorKey: "name",
      },
      {
        header: ({ column }) => <DataTableColumnHeader column={column} title="Acronym" />,
        accessorKey: "acronym",
      },
      {
        header: "Address",
        accessorKey: "address",
      },
      {
        header: "City",
        accessorKey: "city",
      },
      {
        id: "actions",
        header: "Actions",
        cell: () => {
          return (
            <Button variant="ghost" className="p-1 h-auto">
              <span className="sr-only">expand permission selection</span>
              <Pencil className="h-4 w-4" />
            </Button>
          );
        },
      },
    ],
    [],
  );

  return (
    <DataTable
      columns={columns}
      data={query.data?.content ?? defaultData}
      totalElements={query.data?.totalElements ?? -1}
      {...tableProps}
    />
  );
};

export default AdminUserCollegePage;
