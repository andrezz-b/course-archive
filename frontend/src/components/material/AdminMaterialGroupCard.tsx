import { MouseEventHandler, useCallback, useState } from "react";
import { MaterialGroupService } from "@/api/material-group.service.ts";
import { SubmitHandler, useForm } from "react-hook-form";
import {
	MaterialGroup,
	MaterialGroupEditData,
	MaterialGroupEditSchema,
} from "@/types/MaterialGroup.ts";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { Card, CardContent, CardHeader } from "@/components/ui/card.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Check, Pencil, PlusCircle, Trash } from "lucide-react";
import {
	Dialog,
	DialogContent,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
} from "@/components/ui/dialog.tsx";
import MaterialCreateForm from "@/components/material/MaterialCreateForm.tsx";
import AdminMaterialItem from "@/components/material/AdminMaterialItem.tsx";

interface MaterialGroupCardProps {
	group: MaterialGroup;
}

const AdminMaterialGroupCard = ({ group }: MaterialGroupCardProps) => {
	const { mutate: deleteGroup } = MaterialGroupService.useDeleteById();
	const { mutate: updateGroup } = MaterialGroupService.useUpdateById();

	const [isEditing, setIsEditing] = useState(false);
	const [materialFormOpen, setMaterialFormOpen] = useState(false);

	const form = useForm<MaterialGroupEditData>({
		defaultValues: {
			name: group.name,
			displayOrder: group.displayOrder,
			description: group.description ?? "",
		},
		resolver: zodResolver(MaterialGroupEditSchema),
	});

	const closeDialog = useCallback(() => setMaterialFormOpen(false), []);

	const enableEditing: MouseEventHandler = (e) => {
		e.preventDefault();
		setIsEditing(true);
		setTimeout(() => form.setFocus("name"), 0);
	};

	const handleEditSubmit: SubmitHandler<MaterialGroupEditData> = (data) => {
		setIsEditing(false);
		if (data.name !== group.name) {
			updateGroup(
				{ id: group.id, ...data },
				{
					onSuccess: () => toast.success("Group updated successfully"),
				},
			);
		}
	};

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
				<form
					onSubmit={form.handleSubmit(handleEditSubmit)}
					className="flex justify-between border-b pb-2"
				>
					{!isEditing ? (
						<h4 className="text-2xl" onClick={enableEditing}>
							{group.name}
						</h4>
					) : (
						<Input className="text-2xl p-0 w-1/3" {...form.register("name")} />
					)}
					<div className="flex items-center gap-3">
						{isEditing ? (
							<Button variant="ghost" className="p-1 h-auto" type="submit">
								<Check className="w-5 h-5" />
							</Button>
						) : (
							<Button variant="ghost" className="p-1 h-auto" onClick={enableEditing}>
								<Pencil className="w-5 h-5" />
							</Button>
						)}
						<Button variant="ghost" className="p-1 h-auto" onClick={handleDelete}>
							<Trash className="w-5 h-5 text-destructive" />
						</Button>
					</div>
				</form>
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
