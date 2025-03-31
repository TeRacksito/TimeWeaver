import { User } from './user';

export interface Project {
  id: number;
  name: string;
  description: string;
}

export interface ProjectRole {
  id: number;
  name: string;
}

export interface ProjectInvitation {
  id: number;
  invitedUser: User;
  inviterUser: User;
  project: Project;
  projectRole: ProjectRole;
  createdAt: Date;
}
