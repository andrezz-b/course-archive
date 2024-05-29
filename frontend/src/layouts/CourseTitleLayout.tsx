import { Outlet, useNavigate, useParams } from "react-router-dom";
import { CourseService } from "@/api/course.service.ts";
import { CourseYearService } from "@/api/course-year.service.ts";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import { Button } from "@/components/ui/button.tsx";
import { ChevronLeft } from "lucide-react";
import { useMemo } from "react";

const CourseTitleLayout = () => {
	const navigate = useNavigate();
	const { courseYearId, courseId } = useParams<{ courseYearId: string; courseId: string }>();
	const courseQuery = CourseService.useGetById(courseId ? parseInt(courseId) : undefined);
	const courseYearQuery = CourseYearService.useGetById(
		courseYearId ? parseInt(courseYearId) : undefined,
	);

	const isLoading = useMemo(
		() => courseQuery.isLoading || courseYearQuery.isLoading,
		[courseQuery.isLoading, courseYearQuery.isLoading],
	);

	const displayName = useMemo(() => {
		if (isLoading) return "";
		if (!courseYearId) return courseQuery.data?.name;
		return `${courseQuery.data?.name} - ${courseYearQuery.data?.academicYear}`;
	}, [isLoading, courseQuery.data, courseYearQuery.data, courseYearId]);

	return (
		<div>
			{isLoading ? (
				<div className="flex gap-2 my-2">
					<Skeleton className="w-[40px] h-[35px]" />
					<Skeleton className="w-[400px] h-[35px]" />
				</div>
			) : (
				<div className="flex items-center mb-4 gap-4 min-h-[60px]">
					<Button
						variant="outline"
						className="w-8 h-8 p-0 flex-shrink-0"
						onClick={() => navigate(-1)}
					>
						<ChevronLeft />
					</Button>
					<h3 className="flex items-center gap-4 text-xl md:text-3xl">{displayName}</h3>
				</div>
			)}
			<Outlet />
		</div>
	);
};

export default CourseTitleLayout;
