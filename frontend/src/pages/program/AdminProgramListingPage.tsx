import { Program } from "@/types/Program";
import { ColumnDef, PaginationState } from "@tanstack/react-table";
import { DataTable } from "../../components/ui/data-table";
import { Suspense, useMemo, useState } from "react";
import { keepPreviousData } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Pencil } from "lucide-react";
import { ProgramService } from "@/api/program.service";
import GenericForm from "@/components/GenericForm";
import { CollegeCreateSchema } from "@/types/College";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import Loading from "@/components/Loading";

const AdminProgramListingPage = () => {
  const [pagination, setPagination] = useState<PaginationState>({ pageIndex: 0, pageSize: 10 });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRow, setSelectedRow] = useState<Program | undefined>(undefined);
  const query = ProgramService.useGetPrograms(
    { page: pagination.pageIndex, size: pagination.pageSize },
    {
      placeholderData: keepPreviousData,
    },
  );

  const handleEdit = (program: Program) => {
    setSelectedRow(program);
    setDialogOpen(true);
  };

  const handleCreate = () => {
    setSelectedRow(undefined);
    setDialogOpen(true);
  };

  const defaultData = useMemo(() => [], []);

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
      {/* <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent>
          <Suspense fallback={<Loading size="32" />}>
            <GenericForm 
            schema={CollegeCreateSchema}
            defaultValues={selectedRow}
            
            />
          </Suspense>
        </DialogContent>
      </Dialog> */}
    </div>
  );
}

export default AdminProgramListingPage