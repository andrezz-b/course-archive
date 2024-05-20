import { College } from "@/types/College";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "./ui/card";
import { Button } from "./ui/button";
import { useMemo } from "react";
import { cn } from "@/lib/utils";
import { SquareArrowOutUpRight } from "lucide-react";

interface CollegeCardProps {
  college: College;
}

const CollegeCard = ({ college }: CollegeCardProps) => {
  const truncatedDescription = useMemo(() => {
    if (college.description.length > 90) {
      return college.description.slice(0, 90) + "...";
    }
    return college.description;
  }, [college.description]);
  const linkValid = useMemo(() => {
    try {
      new URL(college.website);
      return true;
    } catch {
      return false;
    }
  }, [college.website]);
  return (
    <Card className="min-w-[300px] max-w-[400px] md:w-[325px] flex flex-col max-h-[400px]">
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
        <Button className="place-self-end">More info</Button>
        {linkValid && (
          <Button variant="link" className="px-2">
            <a target="_blank" href={college.website}>
              Website
            </a>
            <SquareArrowOutUpRight className="h-3 w-3 ml-1" />
          </Button>
        )}
      </CardFooter>
    </Card>
  );
};

export default CollegeCard;
