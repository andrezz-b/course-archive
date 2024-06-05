import { Course } from "@/types/Course";
import { ColumnDef } from "@tanstack/react-table";
import { DataTable, DataTableColumnHeader } from "../../components/ui/data-table";
import { CourseService } from "@/api/course.service";
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

const AdminUserCoursePage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRow, setSelectedRow] = useState<Course | undefined>(undefined);
  const { userId } = useParams<{ userId: string }>();
  const userQuery = UserService.useGetById(userId ? parseInt(userId) : undefined);
  const { requestParams, ...tableProps } = useTableControls();
  const query = CourseService.useGetAll(requestParams, {
    placeholderData: keepPreviousData,
  });

  const defaultData = useMemo(() => [], []);
  const handleOpenDialog = (course: Course) => {
    setSelectedRow(course);
    setDialogOpen(true);
  };

  const columns: ColumnDef<Course>[] = useMemo(
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
        header: "Credits",
        accessorKey: "credits",
      },
      {
        header: "Year",
        accessorKey: "year",
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
              objectType={ObjectType.COURSE}
              objectId={selectedRow.id}
              username={userQuery.data.username}
              title={`Permissions - '${selectedRow.name}'`}
            />
          )}
        </DialogContent>
      </Dialog>
    </>
  );
};

export default AdminUserCoursePage;
