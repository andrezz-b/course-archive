export enum SortDirection {
  ASC = "asc",
  DESC = "desc",
}

export type SortValue<T extends string> = `${T}-${SortDirection}`;

export interface WithId {
  id: number;
}

export interface SortOption {
  field: string;
  direction: SortDirection;
  label: string;
  value: string;
}

export interface FilterOption {
  field: string;
  label: string;
}

export const DISPLAY_LISTING_PAGE_SIZE = 6;