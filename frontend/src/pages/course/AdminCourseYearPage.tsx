import { MaterialGroupService } from "@/api/material-group.service";
import { MaterialGroupCreateData, MaterialGroupCreateSchema } from "@/types/MaterialGroup";
import { useNavigate, useParams } from "react-router-dom";
import { ChevronLeft } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useCallback, useState } from "react";
import GenericForm, { SubmitFn } from "@/components/GenericForm";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import Loading from "@/components/Loading";
import { keepPreviousData } from "@tanstack/react-query";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import { CourseService } from "@/api/course.service.ts";
import { CourseYearService } from "@/api/course-year.service.ts";
import AdminMaterialGroupCard from "@/components/material/AdminMaterialGroupCard.tsx";

const AdminCourseYearPage = () => {
	const { courseYearId, courseId } = useParams<{ courseYearId: string; courseId: string }>();

	const courseQuery = CourseService.useGetById(courseId ? parseInt(courseId) : undefined);
	const courseYearQuery = CourseYearService.useGetById(
		courseYearId ? parseInt(courseYearId) : undefined,
	);
	const groupQuery = MaterialGroupService.useGetAll(
		{
			courseYearId,
			size: 100,
		},
		{
			placeholderData: keepPreviousData,
		},
	);
	const { mutate: createGroup } = MaterialGroupService.useCreate();

	const [dialogOpen, setDialogOpen] = useState(false);
	const navigate = useNavigate();

	const handleSubmit: SubmitFn<MaterialGroupCreateData> = (data) =>
		new Promise((resolve) =>
			createGroup(data, {
				onSuccess: () => resolve(undefined),
				onError: (error) => resolve({ type: error.getStatus(), message: error.getErrorMessage() }),
			}),
		);

	const closeDialog = useCallback(() => setDialogOpen(false), []);

	const TitleDisplay = () => {
		const isLoading = courseQuery.isLoading || courseYearQuery.isLoading;
		return isLoading ? (
			<div className="flex gap-2 my-2">
				<Skeleton className="w-[40px] h-[35px]" />
				<Skeleton className="w-[400px] h-[35px]" />
			</div>
		) : (
			<div className="flex items-center justify-between mb-4">
				<h3 className="flex items-center gap-4 text-3xl">
					<Button variant="outline" className="w-8 h-8 p-0" onClick={() => navigate(-1)}>
						<ChevronLeft />
					</Button>
					{courseQuery.data?.name} - {courseYearQuery.data?.academicYear}
				</h3>
				<Button onClick={() => setDialogOpen(true)}>Add group</Button>
			</div>
		);
	};

	if (groupQuery.isLoading) {
		return <Loading />;
	}

	if (groupQuery.isError || !groupQuery.data) {
		return <div>Error</div>;
	}

	return (
		<div className="container">
			<TitleDisplay />
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
		</div>
	);
};

export default AdminCourseYearPage;
