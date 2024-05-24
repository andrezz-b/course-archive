import {
  Program,
  ProgramCreateData,
  ProgramCreateSchema,
  ProgramEditData,
  ProgramEditSchema,
} from "@/types/Program";
import { ColumnDef, PaginationState } from "@tanstack/react-table";
import { DataTable } from "../../components/ui/data-table";
import { useMemo, useState } from "react";
import { keepPreviousData } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Pencil } from "lucide-react";
import { ProgramService } from "@/api/program.service";
import GenericForm from "@/components/GenericForm";
import { Dialog, DialogContent } from "@/components/ui/dialog";

type OnSubmit = (
  data: ProgramCreateData | ProgramEditData,
  id?: number,
) => Promise<{ type: string; message: string } | undefined>;

const AdminProgramListingPage = () => {
  const [pagination, setPagination] = useState<PaginationState>({ pageIndex: 0, pageSize: 10 });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRow, setSelectedRow] = useState<Program | undefined>(undefined);
  const query = ProgramService.useGetAll(
    { page: pagination.pageIndex, size: pagination.pageSize },
    {
      placeholderData: keepPreviousData,
    },
  );
  const { mutate: createProgram } = ProgramService.useCreate();
  const { mutate: updateProgram } = ProgramService.useUpdateById();

  const handleEdit = (program: Program) => {
    setSelectedRow(program);
    setDialogOpen(true);
  };

  const handleCreate = () => {
    setSelectedRow(undefined);
    setDialogOpen(true);
  };

  const closeDialog = () => {
    setDialogOpen(false);
    setSelectedRow(undefined);
  };

  const defaultData = useMemo(() => [], []);

  const handleSubmit: OnSubmit = async (data, id) =>
    new Promise((resolve) => {
      if (ProgramEditSchema.safeParse(data).success && id) {
        updateProgram(
          { id, ...data },
          {
            onSuccess: () => resolve(undefined),
            onError: (error) =>
              resolve({ message: error.getErrorMessage(), type: error.getStatus() }),
          },
        );
      } else if (ProgramCreateSchema.safeParse(data).success && "name" in data) {
        createProgram(data, {
          onSuccess: () => resolve(undefined),
          onError: (error) =>
            resolve({ message: error.getErrorMessage(), type: error.getStatus() }),
        });
      }
    });

  const columns: ColumnDef<Program>[] = useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
      },
      {
        header: "Name",
        accessorKey: "name",
      },
      {
        header: "Duration",
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
            <Button variant="ghost" className="p-1 h-auto" onClick={() => handleEdit(row.original)}>
              <span className="sr-only">edit program</span>
              <Pencil className="h-4 w-4" />
            </Button>
          );
        },
      },
    ],
    [],
  );

  return (
    <div className="container">
      <div className="flex justify-between">
        <h3 className="mb-5">Programs</h3>
        <Button onClick={handleCreate}>Create Program</Button>
      </div>
      <DataTable
        columns={columns}
        data={query.data?.content ?? defaultData}
        setPagination={setPagination}
        pagination={pagination}
        totalElements={query.data?.totalElements ?? -1}
      />
      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent>
          {selectedRow ? (
            <GenericForm<ProgramEditData>
              schema={ProgramEditSchema}
              defaultValues={{
                degreeTitle: selectedRow.degreeTitle ?? "",
                degreeTitleAbbreviation: selectedRow.degreeTitleAbbreviation ?? "",
                description: selectedRow.description ?? "",
                duration: selectedRow.duration ?? undefined,
              }}
              showReset
              closeDialog={closeDialog}
              onSubmit={(data) => handleSubmit(data, selectedRow?.id)}
              title="Edit Program"
            />
          ) : (
            <GenericForm<ProgramCreateData>
              schema={ProgramCreateSchema}
              defaultValues={{
                name: "",
                collegeId: undefined,
                degreeTitle: "",
                degreeTitleAbbreviation: "",
                description: "",
                degreeType: "",
                duration: undefined,
              }}
              closeDialog={closeDialog}
              onSubmit={(data) => handleSubmit(data)}
              title="Create Program"
            />
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AdminProgramListingPage;
