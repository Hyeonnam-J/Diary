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
        email: string;
    };
    content: string;
    createdDate: string;
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
    comments: BoardComment[];
};