import React, { ReactNode, useState, useEffect } from 'react';
import ReactPaginate from 'react-paginate';
import DefaultLayout from "../layouts/DefaultLayout";
import { SERVER_IP, Page } from "../../Config";
import '../../stylesheets/pages/freeBoardPostRead.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";
import { Navigate, useNavigate, useLocation } from 'react-router-dom';
import { FreeBoardPostDetail, FreeBoardComment } from "../../type/FreeBoardPost"
import { user } from "../../auth/auth";

const FreeBoardPostRead = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [userId, setUserId] = useState<string | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);

    const postId = location?.state?.postId;
    const [postDetail, setPostDetail] = useState<FreeBoardPostDetail | null>(null);
    const [comments, setComments] = useState<FreeBoardComment[]>([]);

    const [totalCommentsCount, setTotalCommentsCount] = useState(0);
    const [totalPageCount, setTotalPageCount] = useState(0);
    const [totalBlockCount, setTotalBlockCount] = useState(0);

    const [curPage, setCurPage] = useState(0);

    const [replyingStates, setReplyingStates] = useState<Record<string, boolean>>({});
    const [updatingStates, setUpdatingStates] = useState<Record<string, boolean>>({});

    useEffect(() => {
        setUserId(localStorage.getItem('userId'));
        setAccessToken(localStorage.getItem('accessToken'));

        getTotalCommentsCount();
    }, []);

    useEffect(() => {
        setTotalPageCount(Math.ceil(totalCommentsCount / Page.perPageSize));
    }, [totalCommentsCount]);

    useEffect(() => {
        setTotalBlockCount(Math.ceil(totalPageCount / Page.perBlockSize));
    }, [totalPageCount]);

    useEffect(() => {
        getComments(`/freeBoard/comments/${postId}?page=${curPage}`);
    }, [curPage]);

    useEffect(() => {
        getPostDetail(postId);
    }, []);

    const getTotalCommentsCount = () => {
        const response = fetch(SERVER_IP + `/freeBoard/comments/totalCount/${postId}`, {
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
        fetch(`${SERVER_IP}/freeBoard/post/read/${postId}`, {
            headers: {
            },
            method: 'GET',
        })
        .then(response => response.json())
        .then(body => {
            setPostDetail(body.data);
        })
    }

    const replyPost = async (postDetail: FreeBoardPostDetail | null) => {
        const isAuth = await user(userId || '', accessToken || '');
        if (isAuth) {
            if (postDetail) navigate('/freeBoard/post/reply', { state: { postDetailId: postDetail.id } });
        } else navigate('/signIn');
    }

    const updatePost = async (postDetail: FreeBoardPostDetail | null) => {
        if ((userId || -1) == postDetail?.user.id) {
            navigate('/freeBoard/post/update', { state: { postDetail: postDetail } });
        } else alert('You are not writer');
    }

    const deletePost = async (postDetail: FreeBoardPostDetail | null) => {
        if ((userId || -1) == postDetail?.user.id) {
            fetch(`${SERVER_IP}/freeBoard/post/delete`, {
                headers: {
                    "userId": userId || '',
                    "postDetailId": postId,
                    "Authorization": accessToken || '',
                },
                method: 'DELETE',
            })
            .then(response => {
                navigate('/freeBoard');
            });
        } else alert('You are not writer');
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
        
        fetch(SERVER_IP+"/freeBoard/comment/write", {
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
            getTotalCommentsCount();
            getComments(`/freeBoard/comments/${postId}?page=${curPage}`);
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

        fetch(SERVER_IP+"/freeBoard/comment/reply", {
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
            getTotalCommentsCount();
            getComments(`/freeBoard/comments/${postId}?page=${curPage}`);
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

    const showUpdateCommentFrame = (commentId: string) => {
        if(!userId){
            alert('please sign in');
            return;
        }

        setUpdatingStates((prevStates) => ({
            [commentId]: !prevStates[commentId] || false,
        }));
    }

    const updateComment = (commentId: string) => {
        const updateTextarea = document.querySelector(`#comment-${commentId} #comment-update`) as HTMLTextAreaElement;

        let updatedContent = '';
        if(updateTextarea){
            updatedContent = updateTextarea.value;
        }
        
        const data = {
            content: updatedContent,
        }

        fetch(SERVER_IP+"/freeBoard/comment/update", {
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
            getComments(`/freeBoard/comments/${postId}?page=${curPage}`);
            updateTextarea.value = '';
            showUpdateCommentFrame(commentId);
        })
    }

    const deleteComment = (comment: FreeBoardComment) => {
        if(userId !== comment.user.id.toString()) {
            alert('Unauthorization');
            return;
        }

        fetch(`${SERVER_IP}/freeBoard/comment/delete`, {
            headers: {
                "userId": userId || '',
                "postDetailId": postId,
                "commentId": comment.id.toString(),
                "Authorization": accessToken || '',
            },
            method: 'DELETE',
        })
        .then(response => {
            getTotalCommentsCount();
            getComments(`/freeBoard/comments/${postId}?page=${curPage}`);
        });
    }

    const list = () => {
        navigate('/freeBoard');
    }

    return (
        <DefaultLayout>
            <div id='readFrame' className={Layout.centerFrame}>
                <div id='read-header'>
                    <button onClick={() => replyPost(postDetail)} className={Button.primary}>reply</button>
                    <button onClick={() => updatePost(postDetail)} className={Button.primary}>update</button>
                    <button onClick={() => deletePost(postDetail)} className={Button.inactive}>delete</button>
                </div>
                
                {postDetail !== null && (
                    <>
                        <div id='postDetailTable'>
                            <p id='postDetail-title'>{postDetail.title}</p>
                            <p>{postDetail.user.email}</p>
                            <div id='postDetail-dataAndViewCount'>
                                <p>{postDetail.createdDate}</p>
                                <p>view {postDetail.viewCount}</p>
                            </div>
                            <p id='postDetail-content'>{postDetail.content}</p>
                        </div>
                    </>
                )}

                <span className='seperator'></span>
                
                <div id='comment'>
                    {comments.map((comment) => {
                        const isCurrentUserComment = comment.user.id.toString() === userId;
                        const isReplyingToComment = replyingStates[comment.id] || false;
                        const isUpdatingComment = updatingStates[comment.id] || false;

                        const paddingLeft = 20 * comment.depth;

                        return (
                            <div key={comment.id} id={`comment-${comment.id}`} className='comment-container' style={{ paddingLeft: `${paddingLeft}px` }}>
                                <div id='comment-header'>
                                    <div id='comment-userContainer'>
                                        <div id='comment-user'>{comment.user?.email}</div>
                                        <div id='commnet-date'>{comment.createdDate}</div>
                                    </div>
                                    <div id='comment-btns'>
                                        {comment.depth === 0 && (
                                            <p onClick={() => showReplyCommentFrame( comment.id.toString() )}>reply</p>
                                        )}
                                        {isCurrentUserComment && (
                                            <>
                                                <p onClick={() => showUpdateCommentFrame( comment.id.toString() )}>update</p>
                                                <p onClick={() => deleteComment( comment )}>delete</p>
                                            </>
                                        )}
                                    </div>
                                </div>
                                {!isUpdatingComment && (
                                    <div id='comment-content'>{comment.content}</div>
                                )}
                                {isUpdatingComment && (
                                    <>
                                        <textarea id='comment-update'>{comment.content}</textarea>
                                        <button onClick={ () => updateComment( comment.id.toString() ) } className={Button.primary}>submit</button>
                                    </>
                                )}
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

export default FreeBoardPostRead;