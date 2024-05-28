import { MaterialGroupService } from "@/api/material-group.service";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
	MaterialGroup,
	MaterialGroupCreateData,
	MaterialGroupCreateSchema,
	MaterialGroupEditData,
	MaterialGroupEditSchema,
} from "@/types/MaterialGroup";
import { useNavigate, useParams } from "react-router-dom";
import { Check, ChevronLeft, File, GripVertical, Pencil, PlusCircle, Trash } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useCallback, useState } from "react";
import GenericForm, { SubmitFn } from "@/components/GenericForm";
import {
	Dialog,
	DialogContent,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
} from "@/components/ui/dialog";
import Loading from "@/components/Loading";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@/components/ui/input";
import { keepPreviousData } from "@tanstack/react-query";
import MaterialCreateForm from "@/components/material/MaterialCreateForm.tsx";
import { Material, MaterialEditData } from "@/types/Material.ts";
import { MaterialService } from "@/api/material.service.ts";

const AdminCourseYearPage = () => {
	const { courseYearId } = useParams<{ courseYearId: string }>();
	const [dialogOpen, setDialogOpen] = useState(false);
	const navigate = useNavigate();

	const query = MaterialGroupService.useGetAll(
		{
			courseYearId,
			size: 20,
		},
		{
			placeholderData: keepPreviousData,
		},
	);
	const { mutate: createGroup } = MaterialGroupService.useCreate();
	const handleSubmit: SubmitFn<MaterialGroupCreateData> = (data) =>
		new Promise((resolve) =>
			createGroup(data, {
				onSuccess: () => resolve(undefined),
				onError: (error) => resolve({ type: error.getStatus(), message: error.getErrorMessage() }),
			}),
		);
	const closeDialog = useCallback(() => setDialogOpen(false), []);
	if (query.isLoading) {
		return <Loading />;
	}

	if (query.isError || !query.data) {
		return <div>Error</div>;
	}
	return (
		<div className="container">
			<div className="flex items-center justify-between mb-4">
				<h3 className="flex items-center gap-4 text-3xl">
					<Button variant="outline" className="w-8 h-8 p-0" onClick={() => navigate(-1)}>
						<ChevronLeft />
					</Button>
					AdminCourseYearPage
				</h3>
				<Button onClick={() => setDialogOpen(true)}>Add group</Button>
			</div>
			<div className="space-y-4">
				{query.data.content.map((group) => (
					<MaterialGroupCard key={group.id} group={group} />
				))}
			</div>
			<Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
				<DialogContent>
					<GenericForm<MaterialGroupCreateData>
						schema={MaterialGroupCreateSchema}
						defaultValues={{
							courseYearId: parseInt(courseYearId!),
							name: "",
							displayOrder: query.data.content.length + 1,
							description: "",
						}}
						closeDialog={closeDialog}
						onSubmit={(data) => handleSubmit(data)}
						title="Create Group"
					/>
				</DialogContent>
			</Dialog>
		</div>
	);
};

interface MaterialGroupCardProps {
	group: MaterialGroup;
}

const MaterialGroupCard = ({ group }: MaterialGroupCardProps) => {
	const [isEditing, setIsEditing] = useState(false);
	const [materialFormOpen, setMaterialFormOpen] = useState(false);
	const { mutate: deleteGroup } = MaterialGroupService.useDeleteById();
	const { mutate: updateGroup } = MaterialGroupService.useUpdateById();
	const form = useForm<MaterialGroupEditData>({
		defaultValues: {
			name: group.name,
			displayOrder: group.displayOrder,
			description: group.description ?? "",
		},
		resolver: zodResolver(MaterialGroupEditSchema),
	});

	const closeDialog = useCallback(() => setMaterialFormOpen(false), []);

	const toggleEdit = () => {
		setIsEditing((prev) => !prev);
		if (form.getValues().name !== group.name) {
			updateGroup({ id: group.id, ...form.getValues() });
		}
	};
	return (
		<Card>
			<CardHeader>
				<div className="flex justify-between border-b">
					<CardTitle>
						{!isEditing ? (
							group.name
						) : (
							<Input className="text-2xl p-0" {...form.register("name")} />
						)}
					</CardTitle>
					<div className="flex items-center gap-3">
						<Button variant="ghost" className="p-1 h-auto" onClick={toggleEdit}>
							{isEditing ? <Check className="w-5 h-5" /> : <Pencil className="w-5 h-5" />}
						</Button>
						<Button
							variant="ghost"
							className="p-1 h-auto"
							onClick={() => deleteGroup({ id: group.id })}
						>
							<Trash className="w-5 h-5 text-destructive" />
						</Button>
					</div>
				</div>
			</CardHeader>
			<CardContent>
				<div>
					{group.materials?.map((material) => (
						<MaterialItem key={material.id} material={material} materialGroupId={group.id} />
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

interface MaterialItemProps {
	material: Material;
	materialGroupId: number;
}

const MaterialItem = ({ material, materialGroupId }: MaterialItemProps) => {
	const { mutate: updateMaterial } = MaterialService.useUpdateById();
	const { mutate: getFile } = MaterialService.useGetFile();
	const [isEditing, setIsEditing] = useState(false);
	const form = useForm<MaterialEditData>({
		defaultValues: {
			name: material.name,
			description: material.description ?? "",
			materialGroupId,
		},
	});

	const toggleEdit = () => {
		setIsEditing((prev) => !prev);
		if (form.getValues().name !== material.name) {
			updateMaterial({ id: material.id, ...form.getValues() });
		}
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

	return (
		<div key={material.id} className="flex items-center gap-2 underline px-4 py-2">
			<GripVertical />
			<Button variant="ghost" onClick={openFile} className="p-2 h-auto">
				<File className="w-6 h-6" />
			</Button>

			<div className="flex justify-between w-full">
				<h5 className="text-lg">
					{!isEditing ? (
						material.name
					) : (
						<Input className="p-0 text-lg" {...form.register("name")} />
					)}
				</h5>
				<div className="flex items-center gap-3">
					<Button variant="ghost" className="p-1 h-auto" onClick={toggleEdit}>
						{isEditing ? <Check className="w-5 h-5" /> : <Pencil className="w-5 h-5" />}
					</Button>
					<Button variant="ghost" className="p-1 h-auto">
						<Trash className="w-5 h-5 text-destructive" />
					</Button>
				</div>
			</div>
		</div>
	);
};

export default AdminCourseYearPage;
