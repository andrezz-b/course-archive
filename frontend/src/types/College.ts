import { SortDirection } from "./Common";

export interface College {
  id: number;
  name: string;
  acronym: string;
  city: string;
  postcode: number;
  address: string;
  website: string;
  description: string;
}

export enum CollegeFilterField {
  NAME = "name",
  CITY = "city",
  POSTCODE = "postcode",
  ACROYNM = "acronym",
}

export enum CollegeSortField {
  NAME = "name",
}

export const CollegeSort = [
  {
    field: CollegeSortField.NAME,
    direction: SortDirection.ASC,
    label: "Name: A to Z",
    value: `${CollegeSortField.NAME}-${SortDirection.ASC}` as SortValue,
  },
  {
    field: CollegeSortField.NAME,
    direction: SortDirection.DESC,
    label: "Name: Z to A",
    value: `${CollegeSortField.NAME}-${SortDirection.DESC}` as SortValue,
  },
];

export type SortValue = `${CollegeSortField}-${SortDirection}`;

export const CollegeFilter = [
  {
    field: CollegeFilterField.NAME,
    label: "Name",
  },
  {
    field: CollegeFilterField.CITY,
    label: "City",
  },
  {
    field: CollegeFilterField.POSTCODE,
    label: "Postcode",
  },
  {
    field: CollegeFilterField.ACROYNM,
    label: "Acronym",
  },
];
