import { CollegeService } from "@/api/college.service";
import CollegeCard from "@/components/college/CollegeCard";
import InfiniteCardList from "@/components/InfiniteCardList";
import ListingSearchForm, { SearchData } from "@/components/ListingSearchForm";
import { CollegeFilter, CollegeSort } from "@/types/College";
import { keepPreviousData } from "@tanstack/react-query";
import { useEffect, useCallback } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { useSearchParams } from "react-router-dom";

const CollegeListingPage = () => {
	const [searchParams, setSearchParams] = useSearchParams({
		filterField: CollegeFilter[0].field,
		filterValue: "",
		sortField: CollegeSort[0].field,
		sortDirection: CollegeSort[0].direction,
		page: "0",
	});
	const query = CollegeService.useGetAll(Object.fromEntries(searchParams.entries()), {
		placeholderData: keepPreviousData,
	});

	const form = useForm<SearchData>({
		defaultValues: {
			filterField: searchParams.get("filterField")!,
			filterValue: searchParams.get("filterValue")!,
			sortField: `${searchParams.get("sortField")}-${searchParams.get("sortDirection")}`,
			idField: undefined,
		},
	});

	const setPage = useCallback(
		(page: number) => {
			setSearchParams((prev) => {
				prev.set("page", page.toString());
				return prev;
			});
		},
		[setSearchParams],
	);

	const onSubmit: SubmitHandler<SearchData> = useCallback(
		(data) => {
			setSearchParams({
				filterField: data.filterField,
				filterValue: data.filterValue,
				sortField: data.sortField.split("-")[0],
				sortDirection: data.sortField.split("-")[1],
				page: "0",
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
			<div className="md:min-w-[700px] lg:min-w-[1000px] max-w-[1200px] space-y-4 px-4 md:p-0">
				<h2 className="text-2xl border-none">Browse Colleges</h2>
				<ListingSearchForm
					form={form}
					onSubmit={onSubmit}
					sortOptions={CollegeSort}
					filterOptions={CollegeFilter}
				/>
				<InfiniteCardList
					query={query}
					Card={CollegeCard}
					notFoundMessage="No colleges found"
					setPage={setPage}
				/>
			</div>
		</div>
	);
};

export default CollegeListingPage;
