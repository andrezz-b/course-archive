import { CourseService } from "@/api/course.service";
import CourseCard from "@/components/course/CourseCard";
import InfiniteCardList from "@/components/InfiniteCardList";
import ListingSearchForm, { SearchData } from "@/components/ListingSearchForm";
import { CourseFilter, CourseSort } from "@/types/Course";
import { keepPreviousData } from "@tanstack/react-query";
import { useCallback, useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { useSearchParams } from "react-router-dom";

const CourseListingPage = () => {
  const [searchParams, setSearchParams] = useSearchParams({
    filterField: CourseFilter[0].field,
    filterValue: "",
    sortField: CourseSort[0].field,
    sortDirection: CourseSort[0].direction,
    page: "0",
  });
  const query = CourseService.useGetAll(Object.fromEntries(searchParams.entries()), {
    placeholderData: keepPreviousData,
  });

  const form = useForm<SearchData>({
    defaultValues: {
      filterField: searchParams.get("filterField")!,
      filterValue: searchParams.get("filterValue")!,
      sortField: `${searchParams.get("sortField")}-${searchParams.get("sortDirection")}`,
      idField: searchParams.get("programId") ?? undefined,
    },
  });

  const onSubmit: SubmitHandler<SearchData> = useCallback(
    (data) => {
      setSearchParams({
        filterField: data.filterField,
        filterValue: data.filterValue,
        sortField: data.sortField.split("-")[0],
        sortDirection: data.sortField.split("-")[1],
        programId: data?.idField ?? "",
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
        <h2 className="text-2xl border-none">Browse Courses</h2>
        <ListingSearchForm
          form={form}
          onSubmit={onSubmit}
          sortOptions={CourseSort}
          filterOptions={CourseFilter}
          idFieldName="Program Id"
        />
        <InfiniteCardList
          Card={CourseCard}
          query={query}
          notFoundMessage="No courses found"
          setPage={setPage}
        />
      </div>
    </div>
  );
};

export default CourseListingPage;
