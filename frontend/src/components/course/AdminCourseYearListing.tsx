import { CourseYearService } from "@/api/course-year.service";
import {
  CourseYear,
  CourseYearCreateData,
  CourseYearCreateSchema,
  CourseYearEditData,
  CourseYearEditSchema,
} from "@/types/CourseYear";
import { keepPreviousData } from "@tanstack/react-query";
import { ColumnDef } from "@tanstack/react-table";
import { useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Button } from "../ui/button";
import { ExternalLink, Pencil, Trash } from "lucide-react";
import { DataTable, DataTableColumnHeader } from "../ui/data-table";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import GenericForm from "../GenericForm";
import { useTableControls } from "@/hooks/useTableControls.ts";
import { toast } from "sonner";

type OnSubmit = (
  data: CourseYearCreateData | CourseYearEditData,
  id?: number,
) => Promise<{ type: string; message: string } | undefined>;

const AdminCourseYearListing = () => {
  const { requestParams, ...tableProps } = useTableControls();
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRow, setSelectedRow] = useState<CourseYear | undefined>(undefined);
  const { courseId } = useParams<{ courseId: string }>();

  const query = CourseYearService.useGetAll(
    { courseId: courseId, ...requestParams },
    {
      placeholderData: keepPreviousData,
    },
  );

  const { mutate: createYear } = CourseYearService.useCreate();
  const { mutate: updateYear } = CourseYearService.useUpdateById();
  const { mutate: deleteYear } = CourseYearService.useDeleteById();

  const handleDelete = (id: number) => {
    deleteYear(
      { id },
      {
        onSuccess: () => toast.success("Course year deleted"),
        onError: (error) => toast.error(error.getErrorMessage()),
      },
    );
  };

  const handleEdit = (year: CourseYear) => {
    setSelectedRow(year);
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
      if (CourseYearEditSchema.safeParse(data).success && id) {
        updateYear(
          { id, ...data },
          {
            onSuccess: () => resolve(undefined),
            onError: (error) =>
              resolve({ message: error.getErrorMessage(), type: error.getStatus() }),
          },
        );
      } else if (CourseYearCreateSchema.safeParse(data).success && "courseId" in data) {
        createYear(data, {
          onSuccess: () => resolve(undefined),
          onError: (error) =>
            resolve({ message: error.getErrorMessage(), type: error.getStatus() }),
        });
      }
    });

  const columns: ColumnDef<CourseYear>[] = useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
      },
      {
        header: ({ column }) => <DataTableColumnHeader column={column} title="Academic Year" />,
        accessorKey: "academicYear",
      },
      {
        header: "Enrolled",
        accessorKey: "enrollmentCount",
      },
      {
        header: "Passed",
        accessorKey: "passedCount",
      },
      {
        id: "actions",
        header: "Actions",
        cell: ({ row }) => {
          return (
            <div className="flex items-center">
              <Link to={`./course-year/${row.original.id}`} className="p-1 block cursor-pointer">
                <Button variant="ghost" className="p-1 h-auto">
                  <span className="sr-only">open course year</span>
                  <ExternalLink className="h-4 w-4" />
                </Button>
              </Link>
              <Button
                variant="ghost"
                className="p-1 h-auto"
                onClick={() => handleEdit(row.original)}
              >
                <span className="sr-only">edit course year</span>
                <Pencil className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                className="p-1 h-auto"
                onClick={() => handleDelete(row.original.id)}
              >
                <span className="sr-only">delete course year</span>
                <Trash className="w-4 h-4 text-destructive" />
              </Button>
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
        <h3 className="mb-5">Course Years</h3>
        <Button onClick={handleCreate}>Create Course Year</Button>
      </div>
      <DataTable
        columns={columns}
        data={query.data?.content ?? defaultData}
        totalElements={query.data?.totalElements ?? -1}
        {...tableProps}
      />
      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent>
          {selectedRow ? (
            <GenericForm<CourseYearEditData>
              schema={CourseYearEditSchema}
              defaultValues={{
                professor: selectedRow.professor ?? "",
                assistant: selectedRow.assistant ?? "",
                difficulty: selectedRow.difficulty ?? "",
                enrollmentCount: selectedRow.enrollmentCount ?? "",
                passedCount: selectedRow.passedCount ?? "",
                lectureCount: selectedRow.lectureCount ?? "",
                exerciseCount: selectedRow.exerciseCount ?? "",
                laboratoryCount: selectedRow.laboratoryCount ?? "",
              }}
              showReset
              closeDialog={closeDialog}
              onSubmit={(data) => handleSubmit(data, selectedRow?.id)}
              title="Edit Course Year"
            />
          ) : (
            <GenericForm<CourseYearCreateData>
              schema={CourseYearCreateSchema}
              defaultValues={{
                academicYear: "",
                // @ts-expect-error String must be passed so that the from can be controlled
                courseId: courseId ?? "",
                professor: "",
                assistant: "",
                difficulty: "",
                enrollmentCount: "",
                passedCount: "",
                lectureCount: "",
                exerciseCount: "",
                laboratoryCount: "",
              }}
              closeDialog={closeDialog}
              onSubmit={(data) => handleSubmit(data)}
              title="Create Course Year"
            />
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default AdminCourseYearListing;
