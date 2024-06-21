import { CollegeService } from "@/api/college.service";
import Loading from "@/components/Loading";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardFooter } from "@/components/ui/card";
import { ChevronLeft, SquareArrowOutUpRight } from "lucide-react";
import { useMemo } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { Separator } from "@/components/ui/separator.tsx";

const CollegePage = () => {
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
    <>
      <div className="flex items-center mb-4 gap-4 min-h-[60px]">
        <Button
          variant="outline"
          className="w-8 h-8 p-0 flex-shrink-0"
          onClick={() => navigate(-1)}
        >
          <ChevronLeft />
        </Button>
        <h3 className="flex items-center gap-4 text-xl md:text-3xl">{college.name}</h3>
        <Separator orientation="vertical" className="h-8" />
        <span className="font-bold text-lg md:text-xl text-muted-foreground">
          {college.acronym}
        </span>
      </div>
      <Card className="w-full md:px-10 border-none space-y-4 relative">
        <CardContent className="flex flex-col gap-8">
          <div>
            <h3 className="mb-2">Description</h3>
            <span>{college.description}</span>
          </div>
          <div className="flex flex-col gap-1">
            <h3 className="mb-2">Location</h3>
            <span>
              Address: <strong>{college.address}</strong>
            </span>
            <span>
              Postcode: <strong>{college.postcode}</strong>
            </span>
            <span>
              City: <strong>{college.city}</strong>
            </span>
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
    </>
  );
};

export default CollegePage;
