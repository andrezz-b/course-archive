export enum SortDirection {
  ASC = "asc",
  DESC = "desc",
}

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

export const VALID_FILE_TYPES = ["image/jpeg", "image/png", "application/pdf", "text/markdown", "text/plain"];
