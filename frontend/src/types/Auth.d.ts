interface StateContextType<T> {
  auth: T;
  setAuth: React.Dispatch<React.SetStateAction<T>>;
}
export interface AuthContextData {
  accessToken: string;
}
export type AuthContextType = StateContextType<AuthContextData | null>;

export interface AuthContextProps {
  children: React.ReactNode;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
}

export interface RefreshResponse {
  accessToken: string;
}

export interface RegisterData {
  email: string;
  password: string;
  passwordRepeat: string;
  firstName: string;
  lastName: string;
}

export interface LoginData {
  email: string;
  password: string;
}
