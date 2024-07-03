import { UseQueryResult } from "@tanstack/react-query";
import Loading from "./Loading";
import { ApiError } from "@/api/config/ApiError";
import { Page } from "@/types/Page";
import { Button } from "./ui/button";
import { WithId } from "@/types/Common";
import { useMemo } from "react";
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from "lucide-react";

export interface InfiniteCardProps<T extends WithId> {
  item: T;
}

interface ListProps<T extends WithId> {
  query: UseQueryResult<Page<T>, ApiError>;
  Card: React.ComponentType<InfiniteCardProps<T>>;
  notFoundMessage?: string;
  setPage: (page: number) => void;
}

interface Pagination {
  page: number;
  pageCount: number;
  hasPreviousPage: boolean;
  hasNextPage: boolean;
}

const InfiniteCardList = <T extends WithId>({
  query,
  Card,
  notFoundMessage = "No items found",
  setPage,
}: ListProps<T>) => {
  const pagination: Pagination = useMemo(() => {
    if (!query.data) {
      return {
        page: 0,
        pageCount: 0,
        hasPreviousPage: false,
        hasNextPage: false,
      };
    }
    return {
      page: query.data.number,
      pageCount: query.data.totalPages,
      hasPreviousPage: !query.data.first,
      hasNextPage: !query.data.last,
    };
  }, [query.data]);
  if (query.isLoading) {
    return <Loading />;
  }

  if (query.isError || !query.isSuccess) {
    return <div>Something went wrong...</div>;
  }

  if (!query.data.content?.length) {
    return <h3 className="text-center pt-10">{notFoundMessage}</h3>;
  }

  return (
    <>
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4 justify-items-center">
        {query.data.content.map((item) => (
          <Card key={item.id} item={item} />
        ))}
      </div>
      <div className="flex w-full justify-end">
        <div className="flex w-[100px] items-center justify-center text-sm font-medium">
          Page {pagination.page + 1} of {pagination.pageCount}
        </div>
        <div className="flex items-center space-x-2">
          <Button
            variant="outline"
            className="hidden h-8 w-8 p-0 lg:flex"
            onClick={() => setPage(0)}
            disabled={!pagination.hasPreviousPage}
          >
            <span className="sr-only">Go to first page</span>
            <ChevronsLeft className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            className="h-8 w-8 p-0"
            onClick={() => setPage(pagination.page - 1)}
            disabled={!pagination.hasPreviousPage}
          >
            <span className="sr-only">Go to previous page</span>
            <ChevronLeft className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            className="h-8 w-8 p-0"
            onClick={() => setPage(pagination.page + 1)}
            disabled={!pagination.hasNextPage}
          >
            <span className="sr-only">Go to next page</span>
            <ChevronRight className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            className="hidden h-8 w-8 p-0 lg:flex"
            onClick={() => setPage(pagination.pageCount - 1)}
            disabled={!pagination.hasNextPage}
          >
            <span className="sr-only">Go to last page</span>
            <ChevronsRight className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </>
  );
};

export default InfiniteCardList;
