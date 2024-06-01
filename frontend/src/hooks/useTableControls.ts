import { useState, useMemo } from "react";
import { PaginationState, SortingState, ColumnFiltersState } from "@tanstack/react-table";
import { SortDirection } from "@/types/Common.ts";

export function useTableControls() {
  const [pagination, setPagination] = useState<PaginationState>({ pageIndex: 0, pageSize: 10 });
  const [sorting, setSorting] = useState<SortingState>([]);
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);

  const mappedSort = useMemo(() => {
    const queryObject = {
      sortField: [] as Array<string>,
      sortDirection: [] as Array<SortDirection>,
    };
    sorting.forEach((sort) => {
      queryObject.sortField.push(sort.id);
      queryObject.sortDirection.push(sort.desc ? SortDirection.DESC : SortDirection.ASC);
    });
    return queryObject;
  }, [sorting]);

  const mappedFilters = useMemo(() => {
    const queryObject = {
      filterField: [] as Array<string>,
      filterValue: [] as Array<string>,
    };
    columnFilters.forEach((filter) => {
      queryObject.filterField.push(filter.id);
      queryObject.filterValue.push(filter.value as string);
    });
    return queryObject;
  }, [columnFilters]);

  const mappedPagination = useMemo(() => ({
    page: pagination.pageIndex,
    size: pagination.pageSize,
  }), [pagination]);

  return {
    pagination,
    setPagination,
    sorting,
    setSorting,
    columnFilters,
    setColumnFilters,
    requestParams: {
      ...mappedSort,
      ...mappedFilters,
      ...mappedPagination,
    },
  };
}
