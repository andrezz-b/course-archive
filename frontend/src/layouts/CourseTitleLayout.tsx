import { Outlet, useLocation, useNavigate, useParams } from "react-router-dom";
import { CourseService } from "@/api/course.service.ts";
import { CourseYearService } from "@/api/course-year.service.ts";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import { Button } from "@/components/ui/button.tsx";
import { ChevronLeft, Star } from "lucide-react";
import { useMemo } from "react";
import { cn } from "@/lib/utils.ts";
import { UserService } from "@/api/user.service.ts";
import { toast } from "sonner";

const CourseTitleLayout = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { courseYearId, courseId } = useParams<{ courseYearId: string; courseId: string }>();
  const courseQuery = CourseService.useGetById(courseId ? parseInt(courseId) : undefined);
  const courseYearQuery = CourseYearService.useGetById(
    courseYearId ? parseInt(courseYearId) : undefined,
  );
  const favoriteCoursesQuery = CourseService.useGetFavorites(
    { size: 999 },
    { enabled: !courseYearId },
  );
  const { mutate: addToFavorite } = UserService.useAddCourseToFavorite();
  const { mutate: removeFromFavorite } = UserService.useRemoveCourseFromFavorite();

  const isAdminPage = useMemo(() => location.pathname.includes("admin"), [location.pathname]);

  const isFavorite = useMemo(() => {
    if (!favoriteCoursesQuery.data || courseYearId) return null;
    return favoriteCoursesQuery.data?.content.some((course) => course.id === parseInt(courseId!));
  }, [favoriteCoursesQuery.data, courseYearId, courseId]);

  const isLoading = useMemo(
    () => courseQuery.isLoading || courseYearQuery.isLoading,
    [courseQuery.isLoading, courseYearQuery.isLoading],
  );

  const displayName = useMemo(() => {
    if (isLoading) return "";
    if (!courseYearId) return courseQuery.data?.name;
    return `${courseQuery.data?.name} - ${courseYearQuery.data?.academicYear}`;
  }, [isLoading, courseQuery.data, courseYearQuery.data, courseYearId]);

  const handleFavorite = () => {
    const mutation = isFavorite ? removeFromFavorite : addToFavorite;
    mutation(
      { id: parseInt(courseId!) },
      {
        onError: (error) => toast.error(error.getErrorMessage()),
      },
    );
  };

  return (
    <>
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
          {!courseYearId && !isAdminPage && (
            <Button variant="ghost" className="p-0.5 h-fit" onClick={handleFavorite}>
              <Star
                className={cn({
                  ["fill-yellow-500 stroke-yellow-500"]: isFavorite,
                  ["fill-background stroke-foreground"]: !isFavorite,
                })}
              />
            </Button>
          )}
        </div>
      )}
      <Outlet />
    </>
  );
};

export default CourseTitleLayout;
