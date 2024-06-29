import { useCallback, useState } from "react";
import { MaterialGroupService } from "@/api/material-group.service.ts";
import {
  MaterialGroup,
  MaterialGroupEditData,
  MaterialGroupEditSchema,
} from "@/types/MaterialGroup.ts";
import { toast } from "sonner";
import { Card, CardContent, CardHeader } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Pencil, PlusCircle, Trash } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog.tsx";
import MaterialCreateForm from "@/components/material/MaterialCreateForm.tsx";
import AdminMaterialItem from "@/components/material/AdminMaterialItem.tsx";
import GenericForm, { SubmitFn } from "@/components/GenericForm.tsx";

interface MaterialGroupCardProps {
  group: MaterialGroup;
}

const AdminMaterialGroupCard = ({ group }: MaterialGroupCardProps) => {
  const { mutate: deleteGroup } = MaterialGroupService.useDeleteById();
  const { mutate: updateGroup } = MaterialGroupService.useUpdateById();

  const [editFormOpen, setEditFormOpen] = useState(false);
  const [materialFormOpen, setMaterialFormOpen] = useState(false);

  const handleEditSubmit: SubmitFn<MaterialGroupEditData> = (data) =>
    new Promise((resolve) => {
      updateGroup(
        { id: group.id, ...data },
        {
          onSuccess: () => {
            toast.success("Group updated successfully");
            resolve(undefined);
          },
          onError: (error) => {
            resolve({ type: error.getStatus(), message: error.getErrorMessage() });
          },
        },
      );
    });

  const closeDialog = useCallback(() => setMaterialFormOpen(false), []);

  const handleDelete = useCallback(
    () =>
      deleteGroup(
        { id: group.id },
        {
          onError: (error) => toast.error(error.getErrorMessage()),
        },
      ),
    [group.id, deleteGroup],
  );

  return (
    <Card>
      <CardHeader>
        <Dialog open={editFormOpen} onOpenChange={setEditFormOpen}>
          <div className="flex justify-between border-b pb-2">
            <h4 className="text-2xl">{group.name}</h4>
            <div className="flex items-center gap-3">
              <DialogTrigger asChild>
                <Button variant="ghost" className="p-1 h-auto">
                  <Pencil className="w-5 h-5" />
                </Button>
              </DialogTrigger>
              <DialogContent>
                <GenericForm<MaterialGroupEditData>
                  schema={MaterialGroupEditSchema}
                  defaultValues={{
                    name: group.name,
                    displayOrder: group.displayOrder,
                    description: group.description ?? "",
                  }}
                  onSubmit={handleEditSubmit}
                  title={"Edit Group - " + group.name}
                  closeDialog={() => setEditFormOpen(false)}
                />
              </DialogContent>
              <Button variant="ghost" className="p-1 h-auto" onClick={handleDelete}>
                <Trash className="w-5 h-5 text-destructive" />
              </Button>
            </div>
          </div>
        </Dialog>
      </CardHeader>
      <CardContent>
        <div>
          {group.materials?.map((material) => (
            <AdminMaterialItem key={material.id} material={material} materialGroupId={group.id} />
          ))}
        </div>
        <Dialog open={materialFormOpen} onOpenChange={setMaterialFormOpen}>
          <DialogTrigger asChild>
            <Button
              className="w-full flex justify-center my-2"
              variant="ghost"
              onClick={() => setMaterialFormOpen(true)}
            >
              <PlusCircle className="w-6 h-6" />
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Add material to - {group.name}</DialogTitle>
            </DialogHeader>
            <MaterialCreateForm materialGroupId={group.id} closeDialog={closeDialog} />
          </DialogContent>
        </Dialog>
      </CardContent>
    </Card>
  );
};

export default AdminMaterialGroupCard;
