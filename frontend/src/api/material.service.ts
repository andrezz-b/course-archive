import type { GenericObjectService } from "./GenericObject.service";
import { Material, MaterialCreateData } from "@/types/Material.ts";
import useAxiosPrivate from "@/hooks/useAxiosPrivate.ts";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { ApiError } from "@/api/config/ApiError.ts";

type MaterialService = Pick<
	GenericObjectService<Material, MaterialCreateData, Record<string, string>>,
	"useCreate"
>;

export const MaterialService: MaterialService = {
	useCreate: (mutationOptions) => {
		const axios = useAxiosPrivate();
		const queryClient = useQueryClient();

		return useMutation({
			mutationFn: async ({ file, ...rest }) => {
				const formData = new FormData();
				formData.append("material", new Blob([JSON.stringify(rest)], {type: "application/json"}));
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
};
