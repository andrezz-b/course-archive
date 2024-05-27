import { ColumnDef, PaginationState } from "@tanstack/react-table";
import { DataTable } from "../../components/ui/data-table";
import { useMemo, useState } from "react";
import { keepPreviousData } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import GenericForm from "@/components/GenericForm";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import {
  Course,
  CourseCreateData,
  CourseCreateSchema,
  CourseEditData,
  CourseEditSchema,
} from "@/types/Course";
import { CourseService } from "@/api/course.service";
import { Link } from "react-router-dom";
import { ExternalLink, Pencil } from "lucide-react";

type OnSubmit = (
  data: CourseCreateData | CourseEditData,
  id?: number,
) => Promise<{ type: string; message: string } | undefined>;

const AdminCourseListingPage = () => {
  const [pagination, setPagination] = useState<PaginationState>({ pageIndex: 0, pageSize: 10 });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRow, setSelectedRow] = useState<Course | undefined>(undefined);
  const query = CourseService.useGetAll(
    { page: pagination.pageIndex, size: pagination.pageSize },
    {
      placeholderData: keepPreviousData,
    },
  );
  const { mutate: createCourse } = CourseService.useCreate();
  const { mutate: updateCourse } = CourseService.useUpdateById();

  const handleEdit = (course: Course) => {
    setSelectedRow(course);
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
      if (CourseEditSchema.safeParse(data).success && id) {
        updateCourse(
          { id, ...data },
          {
            onSuccess: () => resolve(undefined),
            onError: (error) =>
              resolve({ message: error.getErrorMessage(), type: error.getStatus() }),
          },
        );
      } else if (CourseCreateSchema.safeParse(data).success && "programId" in data) {
        createCourse(data, {
          onSuccess: () => resolve(undefined),
          onError: (error) =>
            resolve({ message: error.getErrorMessage(), type: error.getStatus() }),
        });
      }
    });

  const columns: ColumnDef<Course>[] = useMemo(
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
            <div className="flex items-center">
              <Button
                variant="ghost"
                className="p-1 h-auto"
                onClick={() => handleEdit(row.original)}
              >
                <span className="sr-only">edit course</span>
                <Pencil className="h-4 w-4" />
              </Button>
              <Link to={`./${row.original.id}`} className="p-1 block cursor-pointer">
                <Button variant="ghost" className="p-1 h-auto">
                  <span className="sr-only">open course</span>
                  <ExternalLink className="h-4 w-4" />
                </Button>
              </Link>
            </div>
          );
        },
      },
    ],
    [],
  );

  return (
    <div className="container">
      <div className="flex justify-between">
        <h3 className="mb-5">Courses</h3>
        <Button onClick={handleCreate}>Create Course</Button>
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
            <GenericForm<CourseEditData>
              schema={CourseEditSchema}
              defaultValues={{
                name: selectedRow.name,
                credits: selectedRow.credits,
                // @ts-expect-error String must be passed so that the from can be controlled
                year: selectedRow.year ?? "",
                acronym: selectedRow.acronym ?? "",
                description: selectedRow.description ?? "",
              }}
              showReset
              closeDialog={closeDialog}
              onSubmit={(data) => handleSubmit(data, selectedRow?.id)}
              title="Edit Course"
            />
          ) : (
            <GenericForm<CourseCreateData>
              schema={CourseCreateSchema}
              defaultValues={{
                // @ts-expect-error String must be passed so that the from can be controlled
                programId: "",
                name: "",
                // @ts-expect-error String must be passed so that the from can be controlled
                credits: "",
                // @ts-expect-error String must be passed so that the from can be controlled
                year: "",
                acronym: "",
                description: "",
              }}
              closeDialog={closeDialog}
              onSubmit={(data) => handleSubmit(data)}
              title="Create Course"
            />
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AdminCourseListingPage;
