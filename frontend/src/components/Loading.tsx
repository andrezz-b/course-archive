import { LoaderCircle } from "lucide-react";

interface LoadingProps {
  size?: string | number;
}

const Loading = ({ size = 60 }: LoadingProps = {}) => {
  return (
    <div className="h-full w-full flex flex-grow flex-col justify-center items-center bg-background">
      <div className="flex flex-col items-center text-3xl gap-3 text-primary">
        <LoaderCircle className="animate-spin" size={size} />
      </div>
    </div>
  );
};

export default Loading;
