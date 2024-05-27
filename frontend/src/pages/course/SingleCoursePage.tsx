import { CourseYearService } from "@/api/course-year.service";
import { CourseService } from "@/api/course.service";
import Loading from "@/components/Loading";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ChevronLeft } from "lucide-react";
import { Link, useNavigate, useParams } from "react-router-dom";

const SingleCoursePage = () => {
  const navigate = useNavigate();
  const { courseId } = useParams<{ courseId: string }>();
  const courseQuery = CourseService.useGetById(courseId ? parseInt(courseId) : undefined);

  const yearsQuery = CourseYearService.useGetAll({
    courseId,
  });

  if (courseQuery.isLoading) {
    return <Loading />;
  }

  if (courseQuery.isError || !courseQuery.isSuccess) {
    return <div>Error</div>;
  }

  const course = courseQuery.data;

  return (
    <div className="container">
      <Card className="w-full md:px-10 border-none space-y-4 relative">
        <Button
          variant="outline"
          className="absolute w-8 h-8 p-0 left-1 md:left-4 md:top-4"
          onClick={() => navigate(-1)}
        >
          <ChevronLeft />
        </Button>
        <CardHeader className="border-b">
          <CardTitle className="flex gap-4 text-4xl items-center">{course.name}</CardTitle>
          <CardDescription className="text-xl font-bold">{course.acronym}</CardDescription>
        </CardHeader>
        <CardContent className="flex flex-col gap-8">
          <div>
            <h3 className="mb-2">General info</h3>
            <div className="md:flex gap-6 space-y-6 md:space-y-0">
              <div className="min-w-fit md:border-r md:pr-4 space-y-2">
                {course.year && (
                  <>
                    <h4>Year</h4>
                    <span>Available at year: {course.year}</span>
                  </>
                )}
                <h4>Credits</h4>
                <span>{course.credits} ECTS</span>
              </div>
              <div>
                <h4>Description</h4>
                <span>{course.description}</span>
              </div>
            </div>
          </div>
          <div>
            <h3 className="mb-4">Available years</h3>
            {yearsQuery.data?.content.map((year) => (
              <Link to={`./course-year/${year.id}`} className="block w-full border rounded-md">
                <Button variant="ghost" className="w-full justify-start">
                  {year.academicYear}
                </Button>
              </Link>
            ))}
          </div>
        </CardContent>
        <CardFooter className="flex justify-between flex-row-reverse"></CardFooter>
      </Card>
    </div>
  );
};

export default SingleCoursePage;
