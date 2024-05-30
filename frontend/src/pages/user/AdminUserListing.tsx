import { ColumnDef, HeaderContext, PaginationState, SortingState } from "@tanstack/react-table";
import { memo, useMemo, useState } from "react";
import { Button } from "@/components/ui/button.tsx";
import { ChevronDown, ChevronsUpDown, ChevronUp, ExternalLink } from "lucide-react";
import { User } from "@/types/User";
import { Link } from "react-router-dom";
import { DataTable } from "@/components/ui/data-table.tsx";
import { UserService } from "@/api/user.service.ts";
import { SortDirection } from "@/types/Common.ts";
import { keepPreviousData } from "@tanstack/react-query";

interface ColumnSortButtonProps {
  sort: ReturnType<HeaderContext<unknown, unknown>["column"]["getIsSorted"]>;
  sortingToggleHandler: ReturnType<
    HeaderContext<unknown, unknown>["column"]["getToggleSortingHandler"]
  >;
}

const ColumnSortButton = memo(({ sort, sortingToggleHandler }: ColumnSortButtonProps) => {
  return (
    <Button
      variant="ghost"
      onClick={sortingToggleHandler}
      className="p-0 h-auto hover:bg-transparent"
    >
      {sort === "asc" ? (
        <ChevronDown className="h-4 w-4" />
      ) : sort === "desc" ? (
        <ChevronUp className="h-4 w-4" />
      ) : (
        <ChevronsUpDown className="h-4 w-4" />
      )}
    </Button>
  );
});

const AdminUserListing = () => {
  const [pagination, setPagination] = useState<PaginationState>({ pageIndex: 0, pageSize: 10 });
  const [sorting, setSorting] = useState<SortingState>([]);
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
  const query = UserService.useGetAll(
    {
      page: pagination.pageIndex,
      size: pagination.pageSize,
      ...mappedSort,
    },
    {
      placeholderData: keepPreviousData,
    },
  );

  const defaultData = useMemo(() => [], []);
  const columns: ColumnDef<User>[] = useMemo(
    () => [
      {
        header: ({ column }) => {
          return (
            <div className="flex items-center gap-2">
              ID
              <ColumnSortButton
                sort={column.getIsSorted()}
                sortingToggleHandler={column.getToggleSortingHandler()}
              />
            </div>
          );
        },
        accessorKey: "id",
      },
      {
        header: ({ column }) => {
          return (
            <div className="flex items-center gap-2">
              Username
              <ColumnSortButton
                sort={column.getIsSorted()}
                sortingToggleHandler={column.getToggleSortingHandler()}
              />
            </div>
          );
        },
        accessorKey: "username",
      },
      {
        header: ({ column }) => {
          return (
            <div className="flex items-center gap-2">
              Email
              <ColumnSortButton
                sort={column.getIsSorted()}
                sortingToggleHandler={column.getToggleSortingHandler()}
              />
            </div>
          );
        },
        accessorKey: "email",
      },
      {
        header: ({ column }) => {
          return (
            <div className="flex items-center gap-2">
              First name
              <ColumnSortButton
                sort={column.getIsSorted()}
                sortingToggleHandler={column.getToggleSortingHandler()}
              />
            </div>
          );
        },
        accessorKey: "firstName",
      },
      {
        header: ({ column }) => {
          return (
            <div className="flex items-center gap-2">
              Last name
              <ColumnSortButton
                sort={column.getIsSorted()}
                sortingToggleHandler={column.getToggleSortingHandler()}
              />
            </div>
          );
        },
        accessorKey: "lastName",
      },
      {
        id: "actions",
        header: "Actions",
        cell: ({ row }) => {
          return (
            <Link to={`./${row.original.id}`}>
              <Button variant="ghost" className="p-1 h-auto">
                <span className="sr-only">edit college</span>
                <ExternalLink className="h-4 w-4" />
              </Button>
            </Link>
          );
        },
      },
    ],
    [],
  );

  return (
    <div className="container">
      <h3 className="mb-5">Users</h3>
      <DataTable
        columns={columns}
        data={query.data?.content ?? defaultData}
        setPagination={setPagination}
        pagination={pagination}
        totalElements={query.data?.totalElements ?? -1}
        setSorting={setSorting}
        sorting={sorting}
      />
    </div>
  );
};

export default AdminUserListing;
