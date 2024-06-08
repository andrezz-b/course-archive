import { MaterialGroupService } from "@/api/material-group.service";
import { MaterialGroupCreateData, MaterialGroupCreateSchema } from "@/types/MaterialGroup";
import { useParams } from "react-router-dom";
import { useCallback, useState } from "react";
import GenericForm, { SubmitFn } from "@/components/GenericForm";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import Loading from "@/components/Loading";
import { keepPreviousData } from "@tanstack/react-query";
import AdminMaterialGroupCard from "@/components/material/AdminMaterialGroupCard.tsx";
import { Button } from "@/components/ui/button.tsx";
import AdminTagListing from "@/components/AdminTagListing.tsx";

const AdminCourseYearPage = () => {
  const { courseYearId } = useParams<{ courseYearId: string }>();
  const groupQuery = MaterialGroupService.useGetAll(
    {
      courseYearId,
      size: 100,
    },
    {
      placeholderData: keepPreviousData,
      throwOnError: true,
    },
  );
  const { mutate: createGroup } = MaterialGroupService.useCreate();

  const [dialogOpen, setDialogOpen] = useState(false);

  const handleSubmit: SubmitFn<MaterialGroupCreateData> = (data) =>
    new Promise((resolve) =>
      createGroup(data, {
        onSuccess: () => resolve(undefined),
        onError: (error) => resolve({ type: error.getStatus(), message: error.getErrorMessage() }),
      }),
    );

  const closeDialog = useCallback(() => setDialogOpen(false), []);

  if (groupQuery.isLoading) {
    return <Loading />;
  }

  if (groupQuery.isError || !groupQuery.isSuccess) {
    return <div>Error</div>;
  }

  return (
    <>
      <div>
        <AdminTagListing />
      </div>
      <div className="w-full flex justify-between my-4">
        <h4>Groups</h4>
        <Button className="" onClick={() => setDialogOpen(true)}>
          Create group
        </Button>
      </div>
      <div className="space-y-4">
        {groupQuery.data.content.map((group) => (
          <AdminMaterialGroupCard key={group.id} group={group} />
        ))}
      </div>
      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent>
          <GenericForm<MaterialGroupCreateData>
            schema={MaterialGroupCreateSchema}
            defaultValues={{
              courseYearId: parseInt(courseYearId!),
              name: "",
              displayOrder: groupQuery.data.content.length + 1,
              description: "",
            }}
            closeDialog={closeDialog}
            onSubmit={(data) => handleSubmit(data)}
            title="Create Group"
          />
        </DialogContent>
      </Dialog>
    </>
  );
};

export default AdminCourseYearPage;
