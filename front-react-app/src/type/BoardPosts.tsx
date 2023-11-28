export type BoardPost = {
    id: number;
    title: string;
    user: {
        email: string;
    };
    createdDate: string;
    viewCount: number;
    depth: number;
};

export const BoardSort = {
    BASIC: "basic"
};

export type BoardPostDetail = {
    id: number;
    title: string;
    content: string;
    user: {
        email: string;
    };
    createdDate: string;
    viewCount: number;
};