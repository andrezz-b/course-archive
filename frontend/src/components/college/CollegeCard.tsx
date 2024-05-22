import { College } from "@/types/College";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "../ui/card";
import { Button } from "../ui/button";
import { useMemo } from "react";
import { cn } from "@/lib/utils";
import { Link } from "react-router-dom";

interface CollegeCardProps {
  college: College;
}

const CollegeCard = ({ college }: CollegeCardProps) => {
  const truncatedDescription = useMemo(() => {
    if (!college.description) return "";
    if (college.description.length > 90) {
      return college.description.slice(0, 90) + "...";
    }
    return college.description;
  }, [college.description]);

  return (
    <Card className="min-w-[300px] max-w-[400px] md:w-[325px] md:h-[365px] flex flex-col max-h-[400px]">
      <CardHeader className="flex flex-col justify-around h-[120px]">
        <CardTitle>{college.name}</CardTitle>
        <CardDescription className="font-bold">{college.acronym}</CardDescription>
      </CardHeader>
      <CardContent className="flex-grow">
        <span className="text-sm block h-[43px] overflow-hidden overflow-ellipsis">
          {college.address}, {college.postcode}, {college.city}
        </span>
        <p className={cn({ "fade-out": truncatedDescription.endsWith("...") }, "overflow-hidden")}>
          {truncatedDescription}
        </p>
      </CardContent>
      <CardFooter className="flex justify-between flex-row-reverse">
        <Link to={`./${college.id}`}>
          <Button className="place-self-end">More info</Button>
        </Link>
      </CardFooter>
    </Card>
  );
};

export default CollegeCard;
