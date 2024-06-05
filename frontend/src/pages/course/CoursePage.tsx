import { CourseYearService } from "@/api/course-year.service";
import { CourseService } from "@/api/course.service";
import Loading from "@/components/Loading";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardFooter } from "@/components/ui/card";
import { Link, useParams } from "react-router-dom";

const CoursePage = () => {
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
    <Card className="w-full md:px-10 border-none space-y-4 relative">
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
          {yearsQuery.isLoading ? (
            <Loading />
          ) : yearsQuery.data?.content.length === 0 ? (
            <span>No previous years available</span>
          ) : null}
          {yearsQuery.data?.content.map((year) => (
            <Link
              to={`./course-year/${year.id}`}
              className="block w-full border rounded-md my-[2px]"
              key={year.id}
            >
              <Button variant="ghost" className="w-full justify-start">
                {year.academicYear}
              </Button>
            </Link>
          ))}
        </div>
      </CardContent>
      <CardFooter className="flex justify-between flex-row-reverse"></CardFooter>
    </Card>
  );
};

export default CoursePage;
