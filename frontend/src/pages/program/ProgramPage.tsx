import { Link, useNavigate, useParams } from "react-router-dom";
import { useMemo } from "react";
import Loading from "@/components/Loading.tsx";
import { Button } from "@/components/ui/button.tsx";
import { ChevronLeft } from "lucide-react";
import { Card, CardContent, CardFooter } from "@/components/ui/card.tsx";
import { ProgramService } from "@/api/program.service.ts";

const ProgramPage = () => {
  const navigate = useNavigate();
  const { programId } = useParams<{ programId: string }>();
  const query = ProgramService.useGetById(programId ? parseInt(programId) : undefined);

  const durationString = useMemo(() => {
    if (!query.data?.duration) {
      return undefined;
    }
    return `${query.data?.duration} Year${query.data?.duration > 1 ? "s" : ""}`;
  }, [query.data?.duration]);

  if (query.isLoading) {
    return <Loading />;
  }

  if (query.isError || !query.isSuccess) {
    return <div>Error</div>;
  }

  const program = query.data;
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
        <h3 className="flex items-center gap-4 text-xl md:text-3xl">{program.name}</h3>
      </div>
      <Card className="w-full md:px-10 border-none space-y-4 relative">
        <CardContent className="flex flex-col gap-8">
          <div className="flex flex-col">
            <h3 className="mb-2">Duration</h3>
            <span>{durationString}</span>
          </div>
          <div>
            <h3 className="mb-2">Description</h3>
            <span>{program.description}</span>
          </div>
          <div className="flex flex-col gap-1">
            <h3 className="mb-2">Degree</h3>
            <span>
              Type: <strong>{program.degreeType}</strong>
            </span>
            <span>
              Title: <strong>{program.degreeTitle}</strong>
            </span>
            <span>
              Abbreviation: <strong>{program.degreeTitleAbbreviation}</strong>
            </span>
          </div>
        </CardContent>
        <CardFooter className="flex justify-between flex-row-reverse">
          <Link
            to={{
              pathname: "/course",
              search: `?programId=${program.id}`,
            }}
          >
            <Button className="w-32">View Courses</Button>
          </Link>
        </CardFooter>
      </Card>
    </>
  );
};

export default ProgramPage;
