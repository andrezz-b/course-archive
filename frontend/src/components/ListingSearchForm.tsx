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
import { SubmitHandler, UseFormReturn } from "react-hook-form";
import { FilterOption, SortOption } from "@/types/Common";

export interface SearchData {
  filterField: string;
  filterValue: string;
  sortField: string;
  idField?: string;
}

interface SearchFormProps {
  form: UseFormReturn<SearchData>;
  onSubmit: SubmitHandler<SearchData>;
  idFieldName?: string;
  sortOptions: Array<SortOption>;
  filterOptions: Array<FilterOption>;
}

const ListingSearchForm = ({
  form,
  onSubmit,
  idFieldName,
  sortOptions,
  filterOptions,
}: SearchFormProps) => {
  return (
    <Form {...form}>
      <form
        className="flex flex-col md:flex-row items-start justify-between gap-2"
        onSubmit={form.handleSubmit(onSubmit)}
      >
        <div className="flex gap-1">
          {idFieldName && (
            <Input
              {...form.register("idField")}
              placeholder={`Enter ${idFieldName}`}
              className="max-w-[70px] md:max-w-[150px]"
            />
          )}
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
                    {filterOptions.map((filter) => (
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
                  {sortOptions.map((sort) => (
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

export default ListingSearchForm;
