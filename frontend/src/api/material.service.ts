import type { GenericObjectService } from "./GenericObject.service";
import { Material, MaterialCreateData, MaterialEditData } from "@/types/Material.ts";
import useAxiosPrivate from "@/hooks/useAxiosPrivate.ts";
import {useMutation, UseMutationResult, useQueryClient} from "@tanstack/react-query";
import { ApiError } from "@/api/config/ApiError.ts";
import { MaterialGroup } from "@/types/MaterialGroup.ts";
import { Page } from "@/types/Page";

type MaterialService = Pick<
	GenericObjectService<Material, MaterialCreateData, MaterialEditData & { id: number }>,
	"useCreate" | "useUpdateById"
> & {
	useGetFile: () => UseMutationResult<Blob, ApiError, number>
};

export const MaterialService: MaterialService = {
	useCreate: (mutationOptions) => {
		const axios = useAxiosPrivate();
		const queryClient = useQueryClient();

		return useMutation({
			mutationFn: async ({ file, ...rest }) => {
				const formData = new FormData();
				formData.append("material", new Blob([JSON.stringify(rest)], { type: "application/json" }));
				formData.append("file", file[0]);

				try {
					const { data } = await axios.postForm<Material>("/material/", formData);
					return data;
				} catch (error) {
					throw new ApiError(error);
				}
			},
			onSuccess: () => {
				queryClient.invalidateQueries({
					queryKey: ["material-group", "all"],
				});
			},
			...mutationOptions,
		});
	},
	useUpdateById: (mutationOptions) => {
		const axios = useAxiosPrivate();
		const queryClient = useQueryClient();

		return useMutation({
			mutationFn: async (data) => {
				const { id, ...rest } = data;

				try {
					const { data } = await axios.put<Material>(`/material/${id}`, rest);
					return data;
				} catch (error) {
					throw new ApiError(error);
				}
			},
			onSuccess: (data) => {
				queryClient.setQueriesData<Page<MaterialGroup>>(
					{
						queryKey: ["material-group", "all"],
						stale: false,
					},
					(oldData) => {
						if (!oldData || !oldData?.content?.length) return oldData;
						return {
							...oldData,
							content: oldData.content.map((group) => ({
								...group,
								materials: group.materials.map((material) => {
									if (material.id === data.id) return data;
									return material;
								}),
							})),
						};
					},
				);
				queryClient.invalidateQueries({
					queryKey: ["material-group", "all"],
				});
			},
			...mutationOptions,
		});
	},
	useGetFile: () => {
		const axios = useAxiosPrivate();

		return useMutation({
			mutationFn: async (id: number) => {
				try {
					const { data } = await axios.get<Blob>(`/material/file/${id}`, {
						responseType: "blob",
					});
					return data;
				} catch (error) {
					throw new ApiError(error);
				}
			},
		});
	}
};
