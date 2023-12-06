import React, { ReactNode, useState, useEffect } from 'react';
import ReactPaginate from 'react-paginate';
import DefaultLayout from "../layouts/DefaultLayout";
import { SERVER_IP, Page } from "../../Config";
import '../../stylesheets/pages/boardPostRead.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";
import { Navigate, useNavigate, useLocation } from 'react-router-dom';
import { BoardPostDetail, BoardComment } from "../../type/BoardPosts"
import { user } from "../../auth/auth";

const Read = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [userId, setUserId] = useState<string | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);

    const postId = location?.state?.postId;
    const [postDetail, setPostDetail] = useState<BoardPostDetail | null>(null);
    const [comments, setComments] = useState<BoardComment[]>([]);

    const [totalCommentsCount, setTotalCommentsCount] = useState(0);
    const [totalPageCount, setTotalPageCount] = useState(0);
    const [totalBlockCount, setTotalBlockCount] = useState(0);

    const [curPage, setCurPage] = useState(0);

    const [replyingStates, setReplyingStates] = useState<Record<string, boolean>>({});

    useEffect(() => {
        setUserId(localStorage.getItem('userId'));
        setAccessToken(localStorage.getItem('accessToken'));

        getTotalCommentsCount();
    });

    useEffect(() => {
        setTotalPageCount(Math.ceil(totalCommentsCount / Page.perPageSize));
    }, [totalCommentsCount]);

    useEffect(() => {
        setTotalBlockCount(Math.ceil(totalPageCount / Page.perBlockSize));
    }, [totalPageCount]);

    useEffect(() => {
        getComments(`/board/comments/${postId}?page=${curPage}`);
    }, [curPage]);

    useEffect(() => {
        getPostDetail(postId);
    }, []);

    const getTotalCommentsCount = () => {
        const response = fetch(SERVER_IP + `/board/comments/totalCommentsCount/${postId}`, {
            method: 'GET',
        })
        .then(response => response.json())
        .then(body => {
            setTotalCommentsCount(body.data);
        })
        .catch(error => {
            console.log(error);
        })
    }

    const getComments = (uri: string) => {
        const response = fetch(SERVER_IP + uri, {
            method: 'GET',
        })
        .then(response => response.json())
        .then(body => {
            setComments(body.data);
        })
        .catch(error => {
            console.log(error);
        })
    }

    const getPostDetail = (postId: number) => {
        fetch(`${SERVER_IP}/board/post/read/${postId}`, {
            headers: {
            },
            method: 'GET',
        })
        .then(response => response.json())
        .then(body => {
            setPostDetail(body.data);
        })
    }

    const replyPost = async (postDetail: BoardPostDetail | null) => {
        const isAuth = await user(userId || '', accessToken || '');
        if (isAuth) {
            if (postDetail) navigate('/board/post/reply', { state: { postDetailId: postDetail.id } });
        } else navigate('/signIn');
    }

    const updatePost = async (postDetail: BoardPostDetail | null) => {
        if ((userId || -1) == postDetail?.user.id) {
            navigate('/board/post/update', { state: { postDetail: postDetail } });
        } else alert('작성자가 아닙니다');
    }

    const deletePost = async (postDetail: BoardPostDetail | null) => {
        if ((userId || -1) == postDetail?.user.id) {
            fetch(`${SERVER_IP}/board/post/delete`, {
                headers: {
                    "userId": userId || '',
                    "postDetailId": postId,
                    "Authorization": accessToken || '',
                },
                method: 'DELETE',
            })
            .then(response => {
                navigate('/board');
            });
        } else alert('작성자가 아닙니다');
    }

    const writeComment = () => {
        if(!userId){
            alert('please sign in');
            return;
        }

        const commentContent = document.querySelector<HTMLTextAreaElement>('#comment-write textarea')?.value;

        const data = {
            content: commentContent,
        }
        
        fetch(SERVER_IP+"/board/comment/write", {
            headers: {
                "Content-Type": 'application/json',
                "userId": userId || '',
                "postDetailId": postId || '',
                "Authorization": accessToken || '',
            },
            method: 'POST',
            body: JSON.stringify(data),
        })
        .then(body => {
            getComments(`/board/comments/${postId}?page=${curPage}`);
            const textarea = document.querySelector<HTMLTextAreaElement>('#comment-write textarea');
            if (textarea) textarea.value = '';
        })
    }

    const replyComment = (commentId: string) => {
        if(!userId){
            alert('please sign in');
            return;
        }

        const commentContent = document.querySelector<HTMLTextAreaElement>('#comment-reply textarea')?.value;

        const data = {
            content: commentContent,
        }

        fetch(SERVER_IP+"/board/comment/reply", {
            headers: {
                "Content-Type": 'application/json',
                "userId": userId || '',
                "postDetailId": postId || '',
                "commentId": commentId || '',
                "Authorization": accessToken || '',
            },
            method: 'POST',
            body: JSON.stringify(data),
        })
        .then(body => {
            getComments(`/board/comments/${postId}?page=${curPage}`);
            const textarea = document.querySelector<HTMLTextAreaElement>('#comment-reply textarea');
            if (textarea) textarea.value = '';
            showReplyCommentFrame(commentId);
        })
    }

    const showReplyCommentFrame = (commentId: string) => {
        setReplyingStates((prevStates) => ({
            [commentId]: !prevStates[commentId] || false,
        }));
    };

    const updateComment = () => {

    }

    const deleteComment = () => {

    }

    const list = () => {
        navigate('/board');
    }

    return (
        <DefaultLayout>
            <div id='readFrame' className={Layout.centerFrame}>
                <div id='read-header'>
                    <button onClick={() => replyPost(postDetail)} className={Button.primary}>reply</button>
                    <button onClick={() => updatePost(postDetail)} className={Button.primary}>update</button>
                    <button onClick={() => deletePost(postDetail)} className={Button.inactive}>delete</button>
                </div>
                <table>
                    {postDetail !== null && (
                        <>
                            <tr>
                                <th>Title</th>
                                <td>{postDetail.title}</td>
                            </tr>
                            <tr>
                                <th>Date</th>
                                <td>{postDetail.createdDate}</td>
                                <th>Writer</th>
                                <td>{postDetail.user.email}</td>
                                <th>View</th>
                                <td>{postDetail.viewCount}</td>
                            </tr>
                            <tr>
                                <td>{postDetail.content}</td>
                            </tr>
                        </>
                    )}
                </table>
                <div id='commentFrame'>
                    {comments.map((comment) => {
                        const isCurrentUserComment = comment.user.id.toString() === userId;
                        const isReplyingToComment = replyingStates[comment.id] || false;
                        const paddingLeft = 20 * comment.depth;

                        return (
                            <div key={comment.id} style={{paddingLeft: `${paddingLeft}px`}}>
                                <div id='comment-header'>
                                    <div id='comment-user'>{comment.user?.email}</div>
                                    <div id='comment-btns'>
                                        {isCurrentUserComment && comment.depth === 0 && (
                                            <>
                                                <p onClick={() => showReplyCommentFrame(comment.id.toString())}>reply</p>
                                                <p onClick={() => updateComment()}>update</p>
                                                <p onClick={() => deleteComment()}>delete</p>
                                            </>
                                        )}
                                    </div>
                                </div>
                                <div id='comment-content'>{comment.content}</div>
                                {isReplyingToComment && (
                                    <div id='comment-reply'>
                                        <textarea></textarea>
                                        <button onClick = { () => replyComment(comment.id.toString()) } className={Button.primary}>submit</button>
                                    </div>
                                )}
                            </div>
                        )
                    })}
                    <div id='comment-footer'>
                        <div id='comment-write'>
                            <textarea></textarea>
                            <button onClick = { () => writeComment() } className={Button.primary}>submit</button>
                        </div>
                        {totalPageCount > 0 && (
                            <ReactPaginate
                                // pageRangeDisplayed={Page.perBlockSize}
                                pageRangeDisplayed={5}
                                marginPagesDisplayed={1}
                                pageCount={totalPageCount}
                                onPageChange={({ selected }) => setCurPage(selected)}
                                containerClassName={'pagination'}
                                activeClassName={'pageActive'}
                                previousLabel="<"
                                nextLabel=">"
                            />
                        )}
                    </div>
                </div>
                <div id='read-footer'>
                    <button id='list' onClick={list} className={Button.primary}>list</button>
                </div>
            </div>
        </DefaultLayout>
    )
}

export default Read;