import { ProgramFilter, ProgramSort } from "@/types/Program";
import { SubmitHandler, useForm } from "react-hook-form";
import { useSearchParams } from "react-router-dom";
import { useCallback, useEffect } from "react";
import { ProgramService } from "@/api/program.service";
import ProgramCard from "@/components/program/ProgramCard";
import InfiniteCardList from "@/components/InfiniteCardList";
import ListingSearchForm, { SearchData } from "@/components/ListingSearchForm";

const ProgramListingPage = () => {
  const [searchParams, setSearchParams] = useSearchParams({
    filterField: ProgramFilter[0].field,
    filterValue: "",
    sortField: ProgramSort[0].field,
    sortDirection: ProgramSort[0].direction,
    page: "0",
  });
  const query = ProgramService.useGetPrograms(Object.fromEntries(searchParams.entries()));

  const form = useForm<SearchData>({
    defaultValues: {
      filterField: searchParams.get("filterField")!,
      filterValue: searchParams.get("filterValue")!,
      sortField: `${searchParams.get("sortField")}-${searchParams.get("sortDirection")}`,
      idField: searchParams.get("collegeId") ?? undefined,
    },
  });

  const onSubmit: SubmitHandler<SearchData> = useCallback(
    (data) => {
      setSearchParams({
        filterField: data.filterField,
        filterValue: data.filterValue,
        sortField: data.sortField.split("-")[0],
        sortDirection: data.sortField.split("-")[1],
        collegeId: data?.idField ?? "",
        page: "0",
      });
    },
    [setSearchParams],
  );

  const setPage = useCallback(
    (page: number) => {
      setSearchParams((prev) => {
        prev.set("page", page.toString());
        return prev;
      });
    },
    [setSearchParams],
  );

  useEffect(() => {
    const subscription = form.watch((_value, { name }) => {
      if (name === "sortField") {
        form.handleSubmit(onSubmit)();
      }
    });
    return () => subscription.unsubscribe();
  }, [form, form.handleSubmit, form.watch, onSubmit]);

  return (
    <div className="flex flex-col justify-center items-center mt-4 pb-12">
      <div className="md:min-w-[700px] lg:min-w-[1000px] max-w-[1200px] space-y-4 px-4 md:p-0">
        <h2 className="text-2xl border-none">Browse Programs</h2>
        <ListingSearchForm
          form={form}
          onSubmit={onSubmit}
          sortOptions={ProgramSort}
          filterOptions={ProgramFilter}
          idFieldName="College Id"
        />
        <InfiniteCardList
          Card={ProgramCard}
          query={query}
          notFoundMessage="No programs found"
          setPage={setPage}
        />
      </div>
    </div>
  );
};

export default ProgramListingPage;
