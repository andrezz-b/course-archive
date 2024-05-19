import { Button } from "@/components/ui/button";
import { ArrowLeft } from "lucide-react";
import { Link, useRouteError } from "react-router-dom";

export default function ErrorPage() {
  const error = useRouteError();
  console.error(error);

  return (
    <div className="bg-background w-full px-2 md:px-0 h-screen flex items-center justify-center">
      <div className="bg-card border border-accent flex flex-col items-center justify-center px-6 md:px-8 lg:px-24 py-8 rounded-lg shadow">
        <h1 className="text-2xl md:text-3xl lg:text-5xl font-bold tracking-wider text-foreground mt-4">
          Oops!
        </h1>
        <p className="text-foreground mt-4 pb-4 border-b-2 text-center">
          Sorry, an unexpected error has occurred.
        </p>
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
