import React, { ReactNode, useState, useEffect } from 'react';
import ReactPaginate from 'react-paginate';
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

    const Page = {
        perPageSize: 10,
        perBlockSize: 10,
    }

    const [userId, setUserId] = useState<string | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);

    const [posts, setPosts] = useState<Post[]>(() => []);

    const [totalPostsCount, setTotalPostsCount] = useState(0);
    const [totalPageCount, setTotalPageCount] = useState(0);
    const [totalBlockCount, setTotalBlockCount] = useState(0);

    const [curPage, setCurPage] = useState(1);

    const [sort, setSort] = useState("id,desc");

    useEffect(() => {
        setUserId(localStorage.getItem('userId'));
        setAccessToken(localStorage.getItem('accessToken'));

        getTotalPostsCount();
    }, []);

    /* 비동기 때문에 나눠야 한다. */
    useEffect(() => {
        setTotalPageCount(Math.ceil(totalPostsCount / Page.perPageSize));
    }, [totalPostsCount]);

    useEffect(() => {
        setTotalBlockCount(Math.ceil(totalPageCount / Page.perBlockSize));
    }, [totalPageCount]);
    /* 비동기 때문에 나눠야 한다. */

    useEffect(() => {
        getPosts(`/board/posts?page=${curPage}&sort=${sort}`);
    }, [curPage, sort]);

    useEffect(() => {
            console.log(posts);
        }, [posts])

    const showPage = () => {
        console.log("totalPostsCount: "+totalPostsCount);
        console.log("totalPageCount: "+totalPageCount);
        console.log("totalBlockCount: "+totalBlockCount);
        console.log("curPage: "+curPage);
        console.log("posts: "+posts);
    }

    const getTotalPostsCount = () => {
        const response = fetch(SERVER_IP+"/board/posts/totalPostsCount", {
            method: 'GET',
        })
        .then(response => response.json())
        .then(body => {
            setTotalPostsCount(body.data);
        })
        .catch(error => {
            console.log(error);
        })
    }

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
            setPosts(body.data);
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
                    <div id='boardHeader-bottom' onClick={ showPage }></div>
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
                            {posts.map((post) => {
                                return (
                                    <tr key={post.id}>
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
                    <div id='boardFooter-top'>
                        <ReactPaginate
                            pageCount={totalPageCount}
                            onPageChange={ ({selected}) => setCurPage(selected + 1)}
                            containerClassName={'pagination'}
                            activeClassName={'active'}
                            previousLabel="<"
                            nextLabel=">"  
                        />
                    </div>
                    <div id='boardFooter-bottom'></div>
                </div>
            </section>
        </DefaultLayout>
    )
}

export default Board;