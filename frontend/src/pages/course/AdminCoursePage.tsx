import { CourseService } from "@/api/course.service";
import AdminCourseYearListing from "@/components/course/AdminCourseYearListing";
import { Button } from "@/components/ui/button";
import { ChevronLeft } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";

const AdminCoursePage = () => {
  const navigate = useNavigate();
  const { courseId } = useParams<{ courseId: string }>();
  const courseQuery = CourseService.useGetById(courseId ? parseInt(courseId) : undefined);

  return (
    <div className="container space-y-4">
      <div className="flex items-center gap-4">
        {courseQuery.data && (
          <>
            <Button
              variant="outline"
              className="w-8 h-8 p-0 left-1 md:left-4 md:top-4"
              onClick={() => navigate(-1)}
            >
              <ChevronLeft />
            </Button>
            <h3 className="text-3xl">{courseQuery.data.name}</h3>
          </>
        )}
      </div>
      <AdminCourseYearListing />
    </div>
  );
};

export default AdminCoursePage;
