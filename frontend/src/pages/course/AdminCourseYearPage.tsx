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
import { Check, ChevronLeft, File, Pencil, Trash } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useCallback, useState } from "react";
import GenericForm, { SubmitFn } from "@/components/GenericForm";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import Loading from "@/components/Loading";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@/components/ui/input";
import { keepPreviousData } from "@tanstack/react-query";
import MaterialCreateForm from "@/components/material/MaterialCreateForm.tsx";

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
				<ul>
					{group.materials?.map((material) => (
						<li key={material.id} className="flex items-center gap-2 underline">
							<File className="w-4 h-4" />
							{material.name}
						</li>
					))}
				</ul>
				<MaterialCreateForm materialGroupId={group.id} />
			</CardContent>
		</Card>
	);
};

export default AdminCourseYearPage;
