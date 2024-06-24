import { CourseService } from "@/api/course.service";
import CourseCard from "@/components/course/CourseCard";
import InfiniteCardList from "@/components/InfiniteCardList";
import ListingSearchForm, { SearchData } from "@/components/ListingSearchForm";
import { Course, CourseFilter, CourseSort } from "@/types/Course";
import {
  keepPreviousData,
  UndefinedInitialDataOptions,
  UseQueryResult,
} from "@tanstack/react-query";
import { useCallback, useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { useSearchParams } from "react-router-dom";
import { Page } from "@/types/Page";
import { ApiError } from "@/api/config/ApiError.ts";

interface CourseListingPageProps {
  fetchFunction?: (
    params?: Record<string, unknown>,
    options?: Omit<UndefinedInitialDataOptions<Page<Course>, ApiError>, "queryKey" | "queryFn">,
  ) => UseQueryResult<Page<Course>, ApiError>;
  showProgramFilter?: boolean;
  title?: string;
}

const CourseListingPage = ({
  fetchFunction = CourseService.useGetAll,
  showProgramFilter = true,
  title = "Browse Courses",
}: CourseListingPageProps) => {
  const [searchParams, setSearchParams] = useSearchParams({
    filterField: CourseFilter[0].field,
    filterValue: "",
    sortField: CourseSort[0].field,
    sortDirection: CourseSort[0].direction,
    page: "0",
  });
  const query = fetchFunction(Object.fromEntries(searchParams.entries()), {
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
    <div className="flex flex-col justify-center items-center">
      <div className="lg:w-[1000px] space-y-4 px-4 md:p-0">
        <h2 className="text-2xl border-none">{title}</h2>
        <ListingSearchForm
          form={form}
          onSubmit={onSubmit}
          sortOptions={CourseSort}
          filterOptions={CourseFilter}
          idFieldName={showProgramFilter ? "Program Id" : undefined}
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
