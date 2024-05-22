import { ProgramFilter, ProgramSort, ProgramSortValue } from "@/types/Program";
import { Button } from "@/components/ui/button";
import { Form, FormField } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Search } from "lucide-react";
import { SubmitHandler, useForm, UseFormReturn } from "react-hook-form";
import { useSearchParams } from "react-router-dom";
import { useCallback, useEffect, useMemo } from "react";
import { ProgramService } from "@/api/program.service";
import ProgramCard from "@/components/program/ProgramCard";
import Loading from "@/components/Loading";

interface SearchData {
  filterField: string;
  filterValue: string;
  sortField: ProgramSortValue;
  collegeId: number;
}

interface SearchFormProps {
  form: UseFormReturn<SearchData>;
  onSubmit: SubmitHandler<SearchData>;
}

const SearchForm = ({ form, onSubmit }: SearchFormProps) => {
  return (
    <Form {...form}>
      <form
        className="flex flex-col md:flex-row items-start justify-between gap-2"
        onSubmit={form.handleSubmit(onSubmit)}
      >
        <div className="flex gap-1">
          <Input
            {...form.register("collegeId")}
            placeholder="Enter college ID"
            className="max-w-[150px]"
          />
          <FormField
            name="filterField"
            control={form.control}
            render={({ field }) => (
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <SelectTrigger className="w-[160px]">
                  <SelectValue placeholder="" />
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    {ProgramFilter.map((filter) => (
                      <SelectItem key={filter.field} value={filter.field}>
                        {filter.label}
                      </SelectItem>
                    ))}
                  </SelectGroup>
                </SelectContent>
              </Select>
            )}
          />
          <div className="relative">
            <Input placeholder="Search" {...form.register("filterValue")} className="pr-9" />
            <Button
              variant="ghost"
              type="submit"
              className="absolute right-0 top-0 flex items-center justify-center p-2"
            >
              <Search className="w-4 h-4" />
            </Button>
          </div>
        </div>
        <FormField
          name="sortField"
          control={form.control}
          render={({ field }) => (
            <Select onValueChange={field.onChange} defaultValue={field.value}>
              <SelectTrigger className="max-w-[200px] overflow-hidden whitespace-nowrap">
                <SelectValue placeholder="" className="overflow-hidden" />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  {ProgramSort.map((sort) => (
                    <SelectItem key={sort.value} value={sort.value}>
                      {sort.label}
                    </SelectItem>
                  ))}
                </SelectGroup>
              </SelectContent>
            </Select>
          )}
        />
      </form>
    </Form>
  );
};

interface ListProps {
  query: ReturnType<typeof ProgramService.useGetAllPrograms>;
}

const ElementList = ({ query }: ListProps) => {
  if (query.isLoading) {
    return <Loading />;
  }

  if (query.isError || !query.isSuccess) {
    return <div>Error: {query.error?.message}</div>;
  }

  if (!query.data.pages.length || !query.data.pages[0].content?.length) {
    return <h3 className="text-center pt-10">No programs found</h3>;
  }

  return (
    <>
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
        {query.data.pages.map((page) =>
          page.content.map((program) => <ProgramCard key={program.id} program={program} />),
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

const ProgramListingPage = () => {
  const [searchParams, setSearchParams] = useSearchParams({
    filterField: ProgramFilter[0].field,
    filterValue: "",
    sortField: ProgramSort[0].field,
    sortDirection: ProgramSort[0].direction,
  });
  const query = ProgramService.useGetAllPrograms(Object.fromEntries(searchParams.entries()));

  const mappedCollegeId = useMemo(() => {
    const collegeId = searchParams.get("collegeId");
    return collegeId ? parseInt(collegeId) : undefined;
  }, [searchParams]);

  const form = useForm<SearchData>({
    defaultValues: {
      filterField: searchParams.get("filterField")!,
      filterValue: searchParams.get("filterValue")!,
      sortField:
        `${searchParams.get("sortField")}-${searchParams.get("sortDirection")}` as ProgramSortValue,
      collegeId: mappedCollegeId,
    },
  });

  const onSubmit: SubmitHandler<SearchData> = useCallback(
    (data) => {
      setSearchParams({
        filterField: data.filterField,
        filterValue: data.filterValue,
        sortField: data.sortField.split("-")[0],
        sortDirection: data.sortField.split("-")[1],
        collegeId: data.collegeId.toString(),
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
        <SearchForm form={form} onSubmit={onSubmit} />
        <ElementList query={query} />
      </div>
    </div>
  );
};

export default ProgramListingPage;
