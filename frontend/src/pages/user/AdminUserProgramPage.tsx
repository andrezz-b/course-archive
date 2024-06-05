import { Program } from "@/types/Program";
import { ColumnDef } from "@tanstack/react-table";
import { DataTable, DataTableColumnHeader } from "../../components/ui/data-table";
import { ProgramService } from "@/api/program.service";
import { useMemo, useState } from "react";
import { keepPreviousData } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Pencil } from "lucide-react";
import { useTableControls } from "@/hooks/useTableControls.ts";
import { Dialog, DialogContent } from "@/components/ui/dialog.tsx";
import PermissionForm from "@/components/PermissionForm.tsx";
import { useParams } from "react-router-dom";
import { UserService } from "@/api/user.service.ts";
import { ObjectType } from "@/types/Common.ts";

const AdminUserProgramPage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRow, setSelectedRow] = useState<Program | undefined>(undefined);
  const { userId } = useParams<{ userId: string }>();
  const userQuery = UserService.useGetById(userId ? parseInt(userId) : undefined);
  const { requestParams, ...tableProps } = useTableControls();
  const query = ProgramService.useGetAll(requestParams, {
    placeholderData: keepPreviousData,
  });

  const defaultData = useMemo(() => [], []);
  const handleOpenDialog = (program: Program) => {
    setSelectedRow(program);
    setDialogOpen(true);
  };

  const columns: ColumnDef<Program>[] = useMemo(
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
        header: ({ column }) => <DataTableColumnHeader column={column} title="Duration" />,
        accessorKey: "duration",
      },
      {
        header: "Degree Type",
        accessorKey: "degreeType",
      },
      {
        header: "Degree Title",
        accessorKey: "degreeTitle",
      },
      {
        id: "actions",
        header: "Actions",
        cell: ({ row }) => {
          return (
            <Button
              variant="ghost"
              className="p-1 h-auto"
              onClick={() => handleOpenDialog(row.original)}
            >
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
    <>
      <DataTable
        columns={columns}
        data={query.data?.content ?? defaultData}
        totalElements={query.data?.totalElements ?? -1}
        {...tableProps}
      />
      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
          {selectedRow && userQuery.data && (
            <PermissionForm
              objectType={ObjectType.PROGRAM}
              objectId={selectedRow.id}
              username={userQuery.data.username}
            />
          )}
        </DialogContent>
      </Dialog>
    </>
  );
};

export default AdminUserProgramPage;
