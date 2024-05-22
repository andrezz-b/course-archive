import { InfiniteData, UseInfiniteQueryResult } from "@tanstack/react-query";
import Loading from "./Loading";
import { ApiError } from "@/api/config/ApiError";
import { Page } from "@/types/Page";
import { Button } from "./ui/button";
import { WithId } from "@/types/Common";

export interface InfiniteCardProps<T extends WithId> {
  item: T;
}

interface ListProps<T extends WithId> {
  query: UseInfiniteQueryResult<InfiniteData<Page<T>>, ApiError>;
  Card: React.ComponentType<InfiniteCardProps<T>>;
  notFoundMessage?: string;
}

const InfiniteCardList = <T extends WithId>({
  query,
  Card,
  notFoundMessage = "No items found",
}: ListProps<T>) => {
  if (query.isLoading) {
    return <Loading />;
  }

  if (query.isError || !query.isSuccess) {
    return <div>Something went wrong...</div>;
  }

  if (!query.data.pages.length || !query.data.pages[0].content?.length) {
    return <h3 className="text-center pt-10">{notFoundMessage}</h3>;
  }

  return (
    <>
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
        {query.data.pages.map((page) =>
          page.content.map((item) => <Card key={item.id} item={item} />),
        )}
      </div>
      {query.hasNextPage && (
        <div className="flex justify-center w-full">
          <Button onClick={() => query.fetchNextPage()} variant="outline" className="w-24">
            {query.isFetchingNextPage ? <Loading size="20" /> : "Load more"}
          </Button>
        </div>
      )}
    </>
  );
};

export default InfiniteCardList;
