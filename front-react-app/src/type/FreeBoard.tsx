export type FreeBoardPosts = {
    id: number;
    title: string;
    user: {
        email: string;
    };
    createdDate: string;
    viewCount: number;
    depth: number;
};

export const FreeBoardSort = {
    BASIC: "basic"
};

export type FreeBoardComment = {
    id: number;
    user: {
        id: number,
        email: string;
    };
    content: string;
    createdDate: string;
    depth: number,
};

export type FreeBoardPostDetail = {
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