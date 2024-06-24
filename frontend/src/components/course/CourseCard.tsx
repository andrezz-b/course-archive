import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "../ui/card";
import { Button } from "../ui/button";
import { useMemo } from "react";
import { cn } from "@/lib/utils";
import { Link } from "react-router-dom";
import { InfiniteCardProps } from "../InfiniteCardList";
import { Course } from "@/types/Course";

const CourseCard = ({ item: course }: InfiniteCardProps<Course>) => {
  const truncatedDescription = useMemo(() => {
    if (!course.description) return "";
    if (course.description.length > 90) {
      return course.description.slice(0, 90) + "...";
    }
    return course.description;
  }, [course.description]);

  return (
    <Card className="min-w-[300px] max-w-[400px] md:w-[325px] md:h-[365px] flex flex-col max-h-[400px]">
      <CardHeader className="flex flex-col justify-around h-[120px]">
        <CardTitle>{course.name}</CardTitle>
        <CardDescription className="font-bold">{course.acronym}</CardDescription>
      </CardHeader>
      <CardContent className="flex-grow">
        <p>{course.credits} ECTS</p>
        <p className={cn({ "fade-out": truncatedDescription.endsWith("...") }, "overflow-hidden")}>
          {truncatedDescription}
        </p>
      </CardContent>
      <CardFooter className="flex justify-between flex-row-reverse">
        <Link to={`/course/${course.id}`}>
          <Button className="place-self-end">More info</Button>
        </Link>
      </CardFooter>
    </Card>
  );
};

export default CourseCard;
