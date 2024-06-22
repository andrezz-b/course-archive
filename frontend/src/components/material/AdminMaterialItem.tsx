import { Material } from "@/types/Material.ts";
import { MaterialService } from "@/api/material.service.ts";
import { useCallback, useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button.tsx";
import { File, Pencil, Trash } from "lucide-react";
import { Dialog, DialogContent, DialogHeader, DialogTrigger } from "@/components/ui/dialog.tsx";
import MaterialCreateForm from "@/components/material/MaterialCreateForm.tsx";

interface AdminMaterialItemProps {
  material: Material;
  materialGroupId: number;
}

const AdminMaterialItem = ({ material, materialGroupId }: AdminMaterialItemProps) => {
  const { mutate: getFile } = MaterialService.useGetFile();
  const { mutate: deleteMaterial } = MaterialService.useDeleteById();
  const [dialogOpen, setDialogOpen] = useState(false);

  const openFile = () => {
    const newWindow = window.open();
    getFile(material.id, {
      onSuccess: (data) => {
        const url = window.URL.createObjectURL(data);
        if (newWindow) newWindow.location.href = url;
      },
    });
  };

  const handleDelete = useCallback(
    () =>
      deleteMaterial(
        { id: material.id },
        {
          onSuccess: () => toast.success("Material deleted successfully"),
          onError: (error) => toast.error(error.getErrorMessage()),
        },
      ),
    [material.id, deleteMaterial],
  );

  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <div className="flex items-center gap-2 px-4 py-2">
        <Button variant="ghost" onClick={openFile} className="p-2 h-auto">
          <File className="w-6 h-6" />
        </Button>

        <div className="flex gap-4">
          <h5>{material.name}</h5>
          <DialogTrigger asChild>
            <Button variant="ghost" className="p-1 h-auto" type="button">
              <Pencil className="w-5 h-5" />
            </Button>
          </DialogTrigger>

          <Button variant="ghost" className="p-1 h-auto" onClick={handleDelete}>
            <Trash className="w-5 h-5 text-destructive" />
          </Button>
          <DialogContent>
            <DialogHeader>Edit - {material.name}</DialogHeader>
            <MaterialCreateForm
              closeDialog={() => setDialogOpen(false)}
              material={material}
              materialGroupId={materialGroupId}
            />
          </DialogContent>
        </div>
      </div>
    </Dialog>
  );
};

export default AdminMaterialItem;
