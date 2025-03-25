export interface Book {
  id: number;
  title: string;
  author: Author;
  genres: Genre[];
  uniqueCode: string;
}

export interface Author {
  id: number;
  name: string;
}

export interface Genre {
  id: number;
  name: string;
  description: string;
}
