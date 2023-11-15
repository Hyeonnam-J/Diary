import React, { ReactNode, useState } from 'react';
import { Link } from 'react-router-dom';

import My from "../fragments/My";
import Nav from "../fragments/Nav";

import '../../stylesheets/common/common.css';
import '../../stylesheets/common/layout.css';

interface LayoutProps {
    children: ReactNode;
}

const menuClickHandler = (uri: string) => {

    const data = {
        accessToken: localStorage.getItem('accessToken'),
    }

    const response = fetch('http://localhost:8080/'+uri, {
        headers: {
            "Content-Type": "application/json",
        },
        method: 'POST',
        body: JSON.stringify(data),
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
    })
    .catch(error => {
        console.log(error);
    })
}

const Layout: React.FC<LayoutProps> = (props) => {
    const [isNavOpen, setNavOpen] = useState(false);

    const receiveNavClick = () => {
        setNavOpen(prevState => !prevState);
    };

    const navWidth = 'var(--nav-contents-width)'
    const navWidthMinus = 'var(--nav-contents-width-minus)'

    return (
        <div className='container'>
            {/* MY 버튼은 종속 없이 늘 그자리에. 속성 absolute. */}
            <My />

            {/* nav-contents html 요소를 Nav.tsx에서 제어하기에는 코드가 복잡해짐
            DefaultLayout에 html 요소를 넣고 display 요소를 제어하는 것이 간편 */}
            <div id='nav-contents-box' style={{ left: isNavOpen ? '0' : navWidthMinus }}>
                <h3 id='nav-greetings'>Welcome</h3>
                <ul id='nav-contents'>
                    <li onClick={() => menuClickHandler('user')}>user</li>
                    <li onClick={() => menuClickHandler('admin')}>admin</li>

                    <li><Link to="/Menu3">Menu3</Link></li>
                    <li><Link to="/Menu4">Menu4</Link></li>
                </ul>
            </div>

            {/* <main style={{ marginLeft: isNavOpen ? navWidth : '0' }}> */}
            <main className='main-default' style={{ left: isNavOpen ? navWidth : '0' }}>

                {/* nav는 상대적 위치로 종속. */}
                <Nav navClick={receiveNavClick} />
                {props.children}

            </main>

        </div>
    );
}

export default Layout;