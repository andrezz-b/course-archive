import {
  College,
  CollegeCreateData,
  CollegeCreateSchema,
  CollegeEditData,
  CollegeEditSchema,
} from "@/types/College";
import { ColumnDef, PaginationState } from "@tanstack/react-table";
import { DataTable } from "../../components/ui/data-table";
import { CollegeService } from "@/api/college.service";
import { useMemo, useState } from "react";
import { keepPreviousData } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Pencil, Trash } from "lucide-react";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import GenericForm from "@/components/GenericForm";
import { toast } from "sonner";

type OnSubmit = (
  data: CollegeCreateData | CollegeEditData,
  id?: number,
) => Promise<{ type: string; message: string } | undefined>;

const AdminCollegesListingPage = () => {
  const [pagination, setPagination] = useState<PaginationState>({ pageIndex: 0, pageSize: 10 });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRow, setSelectedRow] = useState<College | undefined>(undefined);
  const { mutate: deleteCollege } = CollegeService.useDeleteById();
  const { mutate: editCollege } = CollegeService.useUpdateById();
  const { mutate: createCollege } = CollegeService.useCreate();
  const query = CollegeService.useGetAll(
    { page: pagination.pageIndex, size: pagination.pageSize },
    {
      placeholderData: keepPreviousData,
    },
  );

  const handleDelete = (id: number) =>
    deleteCollege(
      { id },
      {
        onSuccess: () => toast.success("College deleted successfully"),
        onError: (error) => toast.error(error.getErrorMessage()),
      },
    );

  const handleEdit = (college: College) => {
    setSelectedRow(college);
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

  const columns: ColumnDef<College>[] = useMemo(
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
        header: "Acronym",
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
        cell: ({ row }) => {
          return (
            <>
              <Button
                variant="ghost"
                className="p-1 h-auto"
                onClick={() => handleEdit(row.original)}
              >
                <span className="sr-only">edit college</span>
                <Pencil className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                className="p-1 h-auto"
                onClick={() => handleDelete(row.original.id)}
              >
                <span className="sr-only">delete college</span>
                <Trash className="w-4 h-4 text-destructive" />
              </Button>
            </>
          );
        },
      },
    ],
    [],
  );

  const handleSubmit: OnSubmit = async (data, id) =>
    new Promise((resolve) => {
      if (CollegeEditSchema.safeParse(data).success && id) {
        editCollege(
          { id, ...data },
          {
            onSuccess: () => resolve(undefined),
            onError: (error) =>
              resolve({ message: error.getErrorMessage(), type: error.getStatus() }),
          },
        );
      } else if (CollegeCreateSchema.safeParse(data).success && "name" in data) {
        createCollege(data, {
          onSuccess: () => resolve(undefined),
          onError: (error) =>
            resolve({ message: error.getErrorMessage(), type: error.getStatus() }),
        });
      }
    });

  return (
    <div className="container">
      <div className="flex justify-between">
        <h3 className="mb-5">Colleges</h3>
        <Button onClick={handleCreate}>Create College</Button>
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
            <GenericForm<CollegeEditData>
              schema={CollegeEditSchema}
              defaultValues={{
                acronym: selectedRow?.acronym,
                city: selectedRow?.city,
                address: selectedRow?.address,
                postcode: selectedRow?.postcode ?? "",
                website: selectedRow?.website ?? "",
                description: selectedRow?.description ?? "",
              }}
              showReset
              closeDialog={closeDialog}
              onSubmit={(data) => handleSubmit(data, selectedRow?.id)}
              title="Edit College"
            />
          ) : (
            <GenericForm<CollegeCreateData>
              schema={CollegeCreateSchema}
              defaultValues={{
                acronym: "",
                city: "",
                name: "",
                address: "",
                // @ts-expect-error String must be passed so that the from can be controlled
                postcode: "",
                website: "",
                description: "",
              }}
              closeDialog={closeDialog}
              onSubmit={(data) => handleSubmit(data)}
              title="Create College"
            />
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AdminCollegesListingPage;
