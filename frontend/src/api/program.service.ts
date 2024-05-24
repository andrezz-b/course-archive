import {
  Program,
  ProgramCreateData,
  ProgramEditData,
} from "@/types/Program";
import {  WithId } from "@/types/Common";
import { createGenericObjectService } from "./GenericObject.service";

// interface GetAllProgramsParams {
//   sortField?: ProgramSortField;
//   sortDirection?: SortDirection;
//   filterField?: ProgramFilterField;
//   filterValue?: string;
//   page?: number;
//   size?: number;
//   collegeId?: number;
// }

// export const ProgramService = {
//   useGetPrograms: <Data extends Page<Program>, Err extends ApiError>(
//     params?: GetAllProgramsParams,
//     options?: Omit<UndefinedInitialDataOptions<Data, Err>, "queryKey" | "queryFn" | "staleTime">,
//   ) => {
//     const axios = useAxiosPrivate();
//     const definedParams = useMemo(() => {
//       if (!params) return {};
//       const definedValues = getDefinedValuesObject(params);
//       definedValues.size = params.size || DISPLAY_LISTING_PAGE_SIZE;
//       return definedValues;
//     }, [params]);

//     return useQuery<Data, Err>({
//       queryKey: ["program", "all", definedParams],
//       queryFn: async () => {
//         try {
//           const { data } = await axios.get<Data>("/program/", {
//             params: definedParams,
//           });
//           return data;
//         } catch (error) {
//           throw new ApiError(error);
//         }
//       },
//       staleTime: 60e3,
//       ...options,
//     });
//   },
// }

export const ProgramService = createGenericObjectService<
  Program,
  ProgramCreateData,
  ProgramEditData & WithId
>({
  entityName: "program",
  entityEndpoint: "program",
});
