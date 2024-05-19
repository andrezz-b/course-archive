export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  roles: Array<Role>;
}

interface Role {
  id: number;
  name: string;
}
