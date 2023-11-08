import DefaultLayout from './layouts/DefaultLayout';
// import React, { useState, useEffect } from 'react';

function Home() {

    const testAuth = async () =>{

        const accessToken = localStorage.getItem('accessToken');
        console.log("home : "+accessToken);

        const data = {
            id: 1,
            email: "hn"
        }

        const url = new URL('http://localhost:8080/testAuth');
        const response = fetch(url, {
            headers: {
                "Content-Type": 'application/json',
                "Authorization": `${accessToken}`
            },
            method: 'POST',
            body: JSON.stringify(data),
        })
        .then(response =>{
            if(response.ok){
                console.log(response.json());
            }
        })

    }

    return (
        <DefaultLayout>
            <button onClick={ testAuth }>권한 검증</button>
        </DefaultLayout>
    );
}

export default Home;
