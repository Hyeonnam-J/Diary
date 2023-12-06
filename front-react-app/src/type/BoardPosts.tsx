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

export type BoardComment = {
    id: number;
    user: {
        id: number,
        email: string;
    };
    content: string;
    createdDate: string;
    depth: number,
};

export type BoardPostDetail = {
    id: number;
    title: string;
    content: string;
    user: {
        id: number,
        email: string;
    };
    createdDate: string;
    viewCount: number;
};