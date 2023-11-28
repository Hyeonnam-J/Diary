import React, { ReactNode, useState, useEffect } from 'react';
import DefaultLayout from "../layouts/DefaultLayout";
import { SERVER_IP } from "../../Config";
import '../../stylesheets/pages/read.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";
import { Navigate, useNavigate, useLocation } from 'react-router-dom';
import { BoardPostDetail } from "../../type/BoardPosts"

const Read = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const postId = location?.state?.postId;
    const [postDetail, setPostDetail] = useState<BoardPostDetail | null>(null);

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

    const replyPost = () => {

    }
    
    const updatePost = () => {

    }
    
    const deletePost = () => {

    }

    return (
        <DefaultLayout>
            <div id='readFrame' className={ Layout.centerFrame }>
                <div id='read-header'>
                    <button onClick={ replyPost } className={ Button.primary }>reply</button>
                    <button onClick={ updatePost } className={ Button.primary }>update</button>
                    <button onClick={ deletePost } className={ Button.inactive }>delete</button>
                </div>
                <div id='d'>
                    {postDetail !== null && (
                        <div>
                            <h1>{postDetail.title}</h1>
                            <p>{postDetail.content}</p>
                        </div>
                    )}
                </div>
            </div>
        </DefaultLayout>
    )
}

export default Read;