import { College } from "@/types/College";
import { ColumnDef, PaginationState } from "@tanstack/react-table";
import { DataTable } from "./data-table";
import { CollegeService } from "@/api/college.service";
import { useMemo, useState } from "react";
import { keepPreviousData } from "@tanstack/react-query";

const columns: ColumnDef<College>[] = [
  {
    header: "ID",
    accessorKey: "id",
  },
  {
    header: "Name",
    accessorKey: "name",
  },
  {
    header: "Acronym",
    accessorKey: "acronym",
  },
  {
    header: "Address",
    accessorKey: "address",
  },
  {
    header: "City",
    accessorKey: "city",
  },
];

const AdminCollegesListingPage = () => {
  const [pagination, setPagination] = useState<PaginationState>({ pageIndex: 0, pageSize: 10 });
  const query = CollegeService.useGetColleges(pagination.pageIndex, pagination.pageSize, {
    placeholderData: keepPreviousData,
  });

  const defaultData = useMemo(() => [], []);

  if (query.isLoading) {
    return <div>Loading...</div>;
  }

  if (query.isError || !query.isSuccess) {
    return <div>Error</div>;
  }

  return (
    <div className="container">
      <h3 className="mb-5">Colleges</h3>
      <DataTable
        columns={columns}
        data={query.data.content ?? defaultData}
        setPagination={setPagination}
        pagination={pagination}
        totalElements={query.data.totalElements}
      />
    </div>
  );
};

export default AdminCollegesListingPage;
