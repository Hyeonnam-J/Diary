export type BoardPost = {
  id: number;
  title: string;
  user: {
      email: string;
  };
  createdDate: string;
  viewCount: number;
};