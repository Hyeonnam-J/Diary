import React, { ReactNode, useState, useEffect } from 'react';
import ReactPaginate from 'react-paginate';
import DefaultLayout from "../layouts/DefaultLayout";
import { SERVER_IP, Page } from "../../Config";
import '../../stylesheets/pages/read.css';
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

    const [comments, setComments] = useState<BoardComment[]>(() => []);

    const postId = location?.state?.postId;
    const [postDetail, setPostDetail] = useState<BoardPostDetail | null>(null);

    const [totalCommentsCount, setTotalCommentsCount] = useState(0);
    const [totalPageCount, setTotalPageCount] = useState(0);
    const [totalBlockCount, setTotalBlockCount] = useState(0);

    const [curPage, setCurPage] = useState(0);

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
        read(postId);
    }, []);

    const getTotalCommentsCount = () => {
        const response = fetch(SERVER_IP+"/board/comments/totalCommentsCount", {
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
        const response = fetch(SERVER_IP+uri, {
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

    const read = (postId: number) => {
        fetch(`${SERVER_IP}/board/read/${postId}`, {
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
        if(isAuth) {
            if (postDetail) navigate('/reply', { state: { postDetailId: postDetail.id } });
        }else navigate('/signIn');
    }
    
    const updatePost = async (postDetail: BoardPostDetail | null) => {
        if((userId || -1) == postDetail?.user.id){
            navigate('/update', { state: { postDetail: postDetail } });
        }else alert('작성자가 아닙니다');
    }
    
    const deletePost = async (postDetail: BoardPostDetail | null) => {
        if((userId || -1) == postDetail?.user.id){
            fetch(`${SERVER_IP}/board/delete`, {
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
        }else alert('작성자가 아닙니다');
    }

    const list = () => {
        navigate('/board');
    }

    return (
        <DefaultLayout>
            <div id='readFrame' className={ Layout.centerFrame }>
                <div id='read-header'>
                    <button onClick={() => replyPost(postDetail)} className={ Button.primary }>reply</button>
                    <button onClick={() => updatePost(postDetail)} className={ Button.primary }>update</button>
                    <button onClick={() => deletePost(postDetail)} className={ Button.inactive }>delete</button>
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
                        return (
                            <div key={comment.id}>
                                <div id='comment-header'>
                                    <div id='comment-user'>{comment.user.email}</div>
                                    <div id='comment-btns'></div>
                                </div>
                                <div id='comment-content'>{comment.content}</div>
                            </div>
                        )
                    })}
                    <div id='comment-footer'>
                        <ReactPaginate
                            // pageRangeDisplayed={Page.perBlockSize}
                            pageRangeDisplayed={5}
                            marginPagesDisplayed={1}
                            pageCount={totalPageCount}
                            onPageChange={ ({selected}) => setCurPage(selected)}
                            containerClassName={'pagination'}
                            activeClassName={'pageActive'}
                            previousLabel="<"
                            nextLabel=">"  
                        />
                    </div>
                </div>
                <div id='read-footer'>
                    <button id='list' onClick={ list } className={ Button.primary }>list</button>
                </div>
            </div>
        </DefaultLayout>
    )
}

export default Read;