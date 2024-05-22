import { College } from "./College";
import { SortDirection, SortValue } from "./Common";

export interface Program {
  id: number;
  name: string;
  duration: number;
  degreeType?: string;
  degreeTitle?: string;
  degreeTitleAbbreviation?: string;
  description?: string;
  College?: College;
}

export enum ProgramFilterField {
  NAME = "name",
  DURATION = "duration",
}

export enum ProgramSortField {
  NAME = "name",
  DURATION = "duration",
}

export type ProgramSortValue = SortValue<ProgramSortField>;
export const ProgramSort = [
  {
    field: ProgramSortField.NAME,
    direction: SortDirection.ASC,
    label: "Name: A to Z",
    value: `${ProgramSortField.NAME}-${SortDirection.ASC}` as ProgramSortValue,
  },
  {
    field: ProgramSortField.NAME,
    direction: SortDirection.DESC,
    label: "Name: Z to A",
    value: `${ProgramSortField.NAME}-${SortDirection.DESC}` as ProgramSortValue,
  },
  {
    field: ProgramSortField.DURATION,
    direction: SortDirection.ASC,
    label: "Duration: Short to Long",
    value: `${ProgramSortField.DURATION}-${SortDirection.ASC}` as ProgramSortValue,
  },
  {
    field: ProgramSortField.DURATION,
    direction: SortDirection.DESC,
    label: "Duration: Long to Short",
    value: `${ProgramSortField.DURATION}-${SortDirection.DESC}` as ProgramSortValue,
  },
];

export const ProgramFilter = [
  {
    field: ProgramFilterField.NAME,
    label: "Name",
  },
  {
    field: ProgramFilterField.DURATION,
    label: "Duration",
  },
];