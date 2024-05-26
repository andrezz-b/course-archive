import { CollegeService } from "@/api/college.service";
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
import { ChevronLeft, SquareArrowOutUpRight } from "lucide-react";
import { useMemo } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

const SingleCollegePage = () => {
  const navigate = useNavigate();
  const { collegeId } = useParams<{ collegeId: string }>();
  const query = CollegeService.useGetById(collegeId ? parseInt(collegeId) : undefined);
  const validHref = useMemo(() => {
    if (!query.data?.website) {
      return undefined;
    }
    try {
      return new URL(query.data?.website);
    } catch {
      return undefined;
    }
  }, [query.data?.website]);

  if (query.isLoading) {
    return <Loading />;
  }

  if (query.isError || !query.isSuccess) {
    return <div>Error</div>;
  }

  const college = query.data;
  return (
    <div className="flex flex-col justify-center items-center mt-6 px-4 pb-2 md:p-0">
      <Card className="md:min-w-[500px] lg:w-[600px] max-w-[1200px] p-2 relative">
        <Button
          variant="outline"
          className="absolute w-10 h-10 p-0 left-3 top-3"
          onClick={() => navigate(-1)}
        >
          <ChevronLeft />
        </Button>
        <CardHeader className="mt-8">
          <CardTitle className="text-3xl">{college.name}</CardTitle>
          <CardDescription className="font-bold text-xl">{college.acronym}</CardDescription>
        </CardHeader>
        <CardContent className="flex flex-col gap-3">
          <h3>Description</h3>
          <span>
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Cupiditate aliquid molestiae
            odit, molestias accusamus dicta, voluptas error quod harum officia sint, aut et maxime?
          </span>
          <h3>Location</h3>
          <div className="flex flex-col">
            <span>Address: {college.address}</span>
            <span>Postcode: {college.postcode}</span>
            <span>City: {college.city}</span>
          </div>
        </CardContent>
        <CardFooter className="flex justify-between flex-row-reverse">
          <Link
            to={{
              pathname: "/program",
              search: `?collegeId=${college.id}`,
            }}
          >
            <Button className="w-32">View Programs</Button>
          </Link>

          {validHref && (
            <a target="_blank" href={validHref.toString()}>
              <Button variant="link" className="p-0 text-xl text-muted-foreground">
                Website
                <SquareArrowOutUpRight size="16" className="ml-1" />
              </Button>
            </a>
          )}
        </CardFooter>
      </Card>
    </div>
  );
};

export default SingleCollegePage;
