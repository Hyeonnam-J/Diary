import React, { ReactNode, useState, useEffect } from 'react';
import DefaultLayout from "../layouts/DefaultLayout";
import { SERVER_IP } from "../../Config";
import '../../stylesheets/pages/read.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";
import { Navigate, useNavigate, useLocation } from 'react-router-dom';
import { BoardPostDetail } from "../../type/BoardPosts"
import { user } from "../../auth/auth";

const Read = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [userId, setUserId] = useState<string | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);

    const postId = location?.state?.postId;
    const [postDetail, setPostDetail] = useState<BoardPostDetail | null>(null);

    useEffect(() => {
        setUserId(localStorage.getItem('userId'));
        setAccessToken(localStorage.getItem('accessToken'));
    })

    useEffect(() => {
        read(postId);
    }, []);

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
    
    const updatePost = () => {

    }
    
    const deletePost = () => {

    }

    const list = () => {
        navigate('/board');
    }

    return (
        <DefaultLayout>
            <div id='readFrame' className={ Layout.centerFrame }>
                <div id='read-header'>
                    <button onClick={() => replyPost(postDetail)} className={ Button.primary }>reply</button>
                    <button onClick={ updatePost } className={ Button.primary }>update</button>
                    <button onClick={ deletePost } className={ Button.inactive }>delete</button>
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
                <div id='read-footer'>
                    <button id='list' onClick={ list } className={ Button.primary }>list</button>
                </div>
            </div>
        </DefaultLayout>
    )
}

export default Read;