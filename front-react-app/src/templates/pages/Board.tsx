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
        createdDate: string;
        viewCount: number;
    };

    const [userId, setUserId] = useState<string | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [posts, setPosts] = useState<Post[]>(() => []);
    const [page, setPage] = useState(1);
    const [sort, setSort] = useState("id,desc");

    useEffect(() => {
        setUserId(localStorage.getItem('userId'));
        setAccessToken(localStorage.getItem('accessToken'));
    }, []);

    useEffect(() => {
        getPosts(`/board/posts?page=${page}&sort=${sort}`);
    }, [page, sort]);

    const getPosts = (uri: string) => {
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
                    <div id='boardHeader-top'></div>
                    <div id='boardHeader-bottom'></div>
                </div>

                <div id='boardSection'>
                    <table>
                        <thead>
                            <tr>
                                <th style={{ width: '5%' }}></th>
                                <th style={{ width: '50%' }}>title</th>
                                <th style={{ width: '20%' }}>email</th>
                                <th style={{ width: '20%' }}>date</th>
                                <th style={{ width: '5%' }}>view</th>
                            </tr>
                        </thead>
                        <tbody>
                            {posts.map((post, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{post.id}</td>
                                        <td className='title'>{post.title}</td>
                                        <td className='email'>{post.user.email}</td>
                                        <td>{post.createdDate}</td>
                                        <td>{post.viewCount}</td>
                                    </tr>
                                )
                            })}
                        </tbody>
                    </table>
                </div>

                <div id='boardFooter'>
                    <div id='boardFooter-top'></div>
                    <div id='boardFooter-bottom'></div>
                </div>
            </section>
        </DefaultLayout>
    )
}

export default Board;