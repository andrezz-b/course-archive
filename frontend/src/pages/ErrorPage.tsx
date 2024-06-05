import { Button } from "@/components/ui/button";
import { ArrowLeft } from "lucide-react";
import { Link, useRouteError } from "react-router-dom";
import { memo, useMemo } from "react";
import { ApiError } from "@/api/config/ApiError.ts";

export default function ErrorPage() {
  const error = useRouteError() as Error;
  console.error(error.message, error);
  const apiError = useMemo(() => (error instanceof ApiError ? error : undefined), [error]);

  return (
    <div className="bg-background w-full px-2 md:px-0 flex items-center justify-center flex-grow flex-col max-h-[800px]">
      <div className="bg-card border border-accent flex flex-col items-center justify-center px-6 md:px-8 lg:px-24 py-8 rounded-lg shadow">
        {apiError?.getStatusCode() === 403 ? <ForbiddenErrorContent /> : <GenericErrorContet />}
        <Link to="/">
          <Button className="mt-4 flex items-center gap-2">
            <ArrowLeft size="15" className="mt-0" />
            Go back to home
          </Button>
        </Link>
      </div>
    </div>
  );
}

const GenericErrorContet = memo(() => (
  <>
    <h2 className="text-2xl md:text-3xl lg:text-5xl font-bold tracking-wider text-foreground mt-4 border-none">
      Oops!
    </h2>
    <p className="text-foreground mt-2 pb-4 border-b-2 text-center">
      Sorry, an unexpected error has occurred.
    </p>
  </>
));

const ForbiddenErrorContent = memo(() => (
  <>
    <h2 className="text-2xl md:text-3xl lg:text-5xl font-bold tracking-wider text-foreground mt-4 border-none">
      Forbidden
    </h2>
    <p className="text-foreground mt-2 pb-4 border-b-2 text-center">
      You don't have permission to access this page.
    </p>
  </>
));
