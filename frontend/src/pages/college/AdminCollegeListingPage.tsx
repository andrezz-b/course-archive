import { College, CollegeCreateData, CollegeCreateSchema } from "@/types/College";
import { ColumnDef, PaginationState } from "@tanstack/react-table";
import { DataTable } from "../../components/ui/data-table";
import { CollegeService } from "@/api/college.service";
import { Suspense, useMemo, useState } from "react";
import { keepPreviousData } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Pencil } from "lucide-react";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import Loading from "@/components/Loading";
import GenericForm from "@/components/GenericForm";

const AdminCollegesListingPage = () => {
  const [pagination, setPagination] = useState<PaginationState>({ pageIndex: 0, pageSize: 10 });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRow, setSelectedRow] = useState<College | undefined>(undefined);
  const query = CollegeService.useGetColleges(
    { page: pagination.pageIndex, size: pagination.pageSize },
    {
      placeholderData: keepPreviousData,
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
            <Button variant="ghost" className="p-1 h-auto" onClick={() => handleEdit(row.original)}>
              <span className="sr-only">edit college</span>
              <Pencil className="h-4 w-4" />
            </Button>
          );
        },
      },
    ],
    [],
  );

  const handleSubmit = async (data, id?: number) => {
    console.log(data, id);

    return undefined;
  }

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
          <Suspense fallback={<Loading size="32" />}>
            {/* <CollegeCreateUpdateDialog
              edit={!!selectedRow}
              college={selectedRow}
              close={() => setDialogOpen(false)}
            /> */}
            <GenericForm<CollegeCreateData>
              schema={CollegeCreateSchema}
              defaultValues={{
                acronym: selectedRow?.acronym,
                city: selectedRow?.city,
                name: selectedRow?.name,
                address: selectedRow?.address,
                postcode: selectedRow?.postcode,
                website: selectedRow?.website,
                description: selectedRow?.description,
              }}
              onSubmit={(data) => handleSubmit(data, selectedRow?.id)}
              title="Create College"
            />
          </Suspense>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AdminCollegesListingPage;
