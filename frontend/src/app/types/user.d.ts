export interface User {
  id:       number;
  username: string;
  email:    null;
  phone:    null;
  roles:    Role[];
}

export interface Role {
  id:   number;
  name: string;
}