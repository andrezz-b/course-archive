import { Material, MaterialEditData } from "@/types/Material.ts";
import { MaterialService } from "@/api/material.service.ts";
import { MouseEventHandler, useCallback, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { toast } from "sonner";
import { Button } from "@/components/ui/button.tsx";
import { Check, File, Pencil, Trash } from "lucide-react";
import { Input } from "@/components/ui/input.tsx";

interface AdminMaterialItemProps {
	material: Material;
	materialGroupId: number;
}

const AdminMaterialItem = ({ material, materialGroupId }: AdminMaterialItemProps) => {
	const { mutate: updateMaterial } = MaterialService.useUpdateById();
	const { mutate: getFile } = MaterialService.useGetFile();
	const { mutate: deleteMaterial } = MaterialService.useDeleteById();

	const [isEditing, setIsEditing] = useState(false);

	const form = useForm<MaterialEditData>({
		defaultValues: {
			name: material.name,
			description: material.description ?? "",
			materialGroupId,
		},
	});

	const handleEditSubmit: SubmitHandler<MaterialEditData> = (data) => {
		setIsEditing(false);
		if (data.name !== material.name) {
			updateMaterial(
				{ id: material.id, ...data },
				{
					onSuccess: () => toast.success("Material updated successfully"),
				},
			);
		}
	};

	const enableEditing: MouseEventHandler = (e) => {
		e.preventDefault();
		setIsEditing(true);
		setTimeout(() => form.setFocus("name"), 0);
	};

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
					onError: (error) => toast.error(error.getErrorMessage()),
				},
			),
		[material.id, deleteMaterial],
	);

	return (
		<div className="flex items-center gap-2 px-4 py-2">
			<Button variant="ghost" onClick={openFile} className="p-2 h-auto">
				<File className="w-6 h-6" />
			</Button>

			<form onSubmit={form.handleSubmit(handleEditSubmit)} className="flex gap-4">
				{isEditing ? (
					<>
						<Input className="p-0 text-lg" {...form.register("name")} />
						<Button variant="ghost" className="p-1 h-auto" type="submit">
							<Check className="w-5 h-5" />
						</Button>
					</>
				) : (
					<>
						<h5 onClick={enableEditing}>{material.name}</h5>
						<Button variant="ghost" className="p-1 h-auto" type="button" onClick={enableEditing}>
							<Pencil className="w-5 h-5" />
						</Button>
					</>
				)}

				<Button variant="ghost" className="p-1 h-auto" onClick={handleDelete}>
					<Trash className="w-5 h-5 text-destructive" />
				</Button>
			</form>
		</div>
	);
};

export default AdminMaterialItem;
