export type FreeBoardPosts = {
    id: number,
    title: string,
    user: {
        email: string,
        nick: string,
    },
    createdDate: string,
    viewCount: number,
    origin: number,
};

export type FreeBoardPostDetail = {
    id: number,
    title: string,
    content: string,
    user: {
        id: number,
        email: string,
        nick: string,
    };
    createdDate: string,
    viewCount: number,
    origin: number,
};

export const FreeBoardSort = {
    BASIC: "basic"
};

export type FreeBoardComment = {
    id: number,
    user: {
        id: number,
        email: string,
        nick: string,
    };
    content: string,
    createdDate: string,
    isParent: boolean,
};
