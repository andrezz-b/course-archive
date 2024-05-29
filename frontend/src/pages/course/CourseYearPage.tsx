import { Card, CardContent, CardDescription, CardHeader } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import { ChevronLeft, File } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import { CourseService } from "@/api/course.service.ts";
import { CourseYearService } from "@/api/course-year.service.ts";
import { MaterialGroupService } from "@/api/material-group.service.ts";
import { keepPreviousData } from "@tanstack/react-query";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import { MaterialGroup } from "@/types/MaterialGroup.ts";
import { Material } from "@/types/Material.ts";
import { MaterialService } from "@/api/material.service.ts";
import {
	Accordion,
	AccordionContent,
	AccordionItem,
	AccordionTrigger,
} from "@/components/ui/accordion.tsx";
import useLocalStorage from "@/hooks/useLocalStorage.ts";
import { useMemo } from "react";

const CourseYearPage = () => {
	const { courseYearId, courseId } = useParams<{ courseYearId: string; courseId: string }>();
	const navigate = useNavigate();
	const courseQuery = CourseService.useGetById(courseId ? parseInt(courseId) : undefined);
	const courseYearQuery = CourseYearService.useGetById(
		courseYearId ? parseInt(courseYearId) : undefined,
	);
	const defaultValue = useMemo(() => ({}), []);
	const [openValues, setOpenValues] = useLocalStorage<Record<string, Array<string>>>(
		"open-group-values",
		defaultValue,
	);

	const currentOpenValues = useMemo(() => {
		if (!openValues) {
			return [];
		}
		if (!openValues[courseYearId!]) {
			return [];
		}
		return openValues[courseYearId!];
	}, [openValues]);

	const updateValues = (values: Array<string>) => {
		setOpenValues({
			...openValues,
			[courseYearId!]: values,
		});
	};

	const groupQuery = MaterialGroupService.useGetAll(
		{
			courseYearId,
			size: 100,
		},
		{
			placeholderData: keepPreviousData,
		},
	);

	const TitleDisplay = () => {
		const isLoading = courseQuery.isLoading || courseYearQuery.isLoading;
		return isLoading ? (
			<div className="flex gap-2 my-2">
				<Skeleton className="w-[40px] h-[35px]" />
				<Skeleton className="w-[400px] h-[35px]" />
			</div>
		) : (
			<>
				<Button
					variant="outline"
					className="absolute w-8 h-8 p-0 left-1 md:left-4 md:top-4"
					onClick={() => navigate(-1)}
				>
					<ChevronLeft />
				</Button>
				<CardHeader>
					<h2 className="flex gap-4 text-4xl items-center border-none p-0 pt-4">
						{courseQuery.data?.name} - {courseYearQuery.data?.academicYear}
					</h2>
				</CardHeader>
			</>
		);
	};

	return (
		<div className="container">
			<Card className="w-full md:px-10 border-none space-y-4 relative">
				<TitleDisplay />
				<CardContent className="flex flex-col gap-8 p-0">
					<Accordion type="multiple" value={currentOpenValues} onValueChange={updateValues}>
						{groupQuery.data?.content.map((group) => (
							<MaterialGroupCard key={group.id} group={group} />
						))}
					</Accordion>
				</CardContent>
			</Card>
		</div>
	);
};

interface MaterialGroupCardProps {
	group: MaterialGroup;
}

const MaterialGroupCard = ({ group }: MaterialGroupCardProps) => {
	return (
		<AccordionItem value={group.id.toString()}>
			<AccordionTrigger>
				<h3 className="text-2xl">{group.name}</h3>
			</AccordionTrigger>
			<AccordionContent>
				{group.description && <CardDescription>{group.description}</CardDescription>}
				<div>
					{group.materials?.map((material) => (
						<MaterialItem key={material.id} material={material} />
					))}
				</div>
			</AccordionContent>
		</AccordionItem>
	);
};

interface MaterialItemProps {
	material: Material;
}

const MaterialItem = ({ material }: MaterialItemProps) => {
	const { mutate: getFile } = MaterialService.useGetFile();
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
		<div className="flex items-center gap-2 p-4 border rounded-md my-2">
			<Button variant="link" onClick={openFile} className="p-2 h-auto flex items-center gap-4">
				<File className="w-6 h-6" />
				<h4>{material.name}</h4>
			</Button>
		</div>
	);
};

export default CourseYearPage;
