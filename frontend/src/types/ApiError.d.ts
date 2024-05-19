export interface ApiError {
  status: string;
  statusCode: number;
  errors: Array<string>;
  timestamp: string;
}
