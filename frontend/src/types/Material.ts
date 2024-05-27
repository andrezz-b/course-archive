export interface Material {
  id: number;
  name: string;
  description: string | null;
  createdAt: string;
  updatedAt: string;
  files: Array<MaterialFile>;
}

export interface MaterialFile {
  id: number;
  name: string;
  path: string;
  mimeType: string;
}