export enum SortDirection {
  ASC = "asc",
  DESC = "desc",
}

export type SortValue<T extends string> = `${T}-${SortDirection}`;