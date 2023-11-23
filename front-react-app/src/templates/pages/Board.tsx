import React, { ReactNode, useState, useEffect } from 'react';
import DefaultLayout from '../layouts/DefaultLayout';

import { SERVER_IP } from "../../Config";

import '../../stylesheets/pages/Board.css';
import Button from "../../stylesheets/modules/button.module.css";

const Board = () => {

    type Post = {
        id: number;
        title: string;
        user: {
            email: string;
        };
        date: string;
        viewCount: number;
        // ... 기타 속성
    };

    const [userId, setUserId] = useState<string | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [posts, setPosts] = useState<Post[]>(() => []);

    useEffect(() => {
        setUserId(localStorage.getItem('userId'));
        setAccessToken(localStorage.getItem('accessToken'));

        getAllPosts('/board/posts/all');
    }, []);

    const getAllPosts = (uri: string) => {
//     const getAllPosts = (uri: string, userId: string | null, accessToken: string | null) => {
    //     const fullUri = userId ? `${uri}/${userId}` : uri;
        const response = fetch(SERVER_IP+uri, {
//             headers: {
//                 "userId": `${userId}`,
//                 'Authorization': `${accessToken}`,
//             },
            method: 'GET',
        })
        .then(response => response.json())
        .then(body => {
            console.log(body);
            console.log(body.data);

            setPosts(body.data);
            console.log(posts);
        })
        .catch(error => {
            console.log(error);
        })
    }

    return (
        <DefaultLayout>
            <section>
                <div id='boardHeader'>
                </div>

                <div id='boardSection'>
                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th>title</th>
                                <th>email</th>
                                <th>date</th>
                                <th>view</th>
                            </tr>
                        </thead>
                        <tbody>
                            {posts.map((post, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{post.id}</td>
                                        <td>{post.title}</td>
                                        <td>{post.user.email}</td>
                                        <td>{post.date}</td>
                                        <td>{post.viewCount}</td>
                                    </tr>
                                )
                            })}
                        </tbody>
                    </table>
                </div>

                <div id='boardFooter'>
                </div>
            </section>
        </DefaultLayout>
    )
}

export default Board;