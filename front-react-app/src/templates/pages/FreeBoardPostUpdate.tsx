import React, { ReactNode, useState, useEffect } from 'react';
import DefaultLayout from "../layouts/DefaultLayout";
import { SERVER_IP } from "../../Config";
import '../../stylesheets/pages/freeBoardPostUpdate.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";
import { useNavigate, useLocation } from 'react-router-dom';
import { FreeBoardPostDetail } from "../../type/FreeBoardPost"

const FreeBoardPostUpdate = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const postDetail: FreeBoardPostDetail = location?.state?.postDetail;
    const [userId, setUserId] = useState<string | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);

    useEffect(() => {
        setUserId(localStorage.getItem('userId'));
        setAccessToken(localStorage.getItem('accessToken'));
    }, []);

    const update = () => {
        const title = document.querySelector('input[name="update-title"]') as HTMLInputElement;
        const content = document.querySelector('textarea[name="update-content"]') as HTMLInputElement;

        const data = {
            title: title.value,
            content: content.value
        }

        fetch(SERVER_IP+"/freeBoard/post/update", {
            headers: {
                "Content-Type": 'application/json',
                "userId": userId || '',
                "postDetailId": postDetail.id.toString(),
                "Authorization": accessToken || '',
            },
            method: 'PUT',
            body: JSON.stringify(data),
        })
        .then(response => {
            navigate('/freeBoard');
        });
    }
    return (
        <DefaultLayout>
            <div id='update-frame'>
                <div id='update-space'></div>
                <div id='update-header'>
                    <button onClick={ update } className={ Button.primary }>submit</button>
                </div>
                <input type="text" id='update-title' name="update-title" defaultValue={postDetail?.title || ''} />
                <div id='update-custom'></div>
                <textarea id='update-content' name="update-content" defaultValue={postDetail?.content || ''} />
            </div>
        </DefaultLayout>
    )
}
    
export default FreeBoardPostUpdate;