import { isAxiosError } from "axios";

export interface ApiErrorData {
  status: string;
  statusCode: number;
  errors: Array<string>;
  timestamp: string;
}

export class ApiError extends Error {

  public status: string = "UNKNOWN";
  public statusCode: number = 0;
  public errors: Array<string> = ["Something went wrong"];
  public timestamp: Date = new Date();

  constructor(error: unknown) {
    super();
    this.name = "ApiError";
    if (isAxiosError<ApiErrorData>(error) && error.response?.data !== undefined) {
      this.init(error.response.data);
    } else if (ApiError.isApiErrorData(error)) {
      this.init(error);
    }
  }

  private init(errorData: ApiErrorData) {
    this.status = errorData.status;
    this.statusCode = errorData.statusCode;
    this.errors = errorData.errors;
    this.timestamp = new Date(errorData.timestamp);
  }

  public getErrorMessage(separator = ", "): string {
    return this.errors.join(separator);
  }

  override get message(): string {
    return `ApiError: ${this.status} (${this.statusCode}) - ${this.getErrorMessage()}`;
  }

  public getStatus(): string {
    return this.status;
  }

  public getStatusCode(): number {
    return this.statusCode;
  }

  public getErrors(): Array<string> {
    return this.errors;
  }

  public getTimestamp(): Date {
    return this.timestamp;
  }

  private static isApiErrorData(errorData: unknown): errorData is ApiErrorData {
    if (!errorData) {
      return false;
    }
    const apiErrorData = errorData as ApiErrorData;
    return (
      apiErrorData.status !== undefined &&
      apiErrorData.statusCode !== undefined &&
      apiErrorData.errors !== undefined &&
      Array.isArray(apiErrorData.errors) &&
      apiErrorData.timestamp !== undefined
    );
  }
}
