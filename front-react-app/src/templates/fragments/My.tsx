import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import my from '../../assets/imgs/my.png'

import '../../stylesheets/common/common.css';
import '../../stylesheets/fragments/my.css';

const My: React.FC = () => {
    // React Hook인 useState를 사용하여 새로운 상태 변수를 생성
    // 변수명 isDropdownVisible, 초깃값 false.
    // 이 변수를 변경하기 위한 함수 setDropdownVisible.
    // useState를 담은 변수 isDropdownVisible의 상태가 바뀔 때마다
    // 해당 값에 의존하는 JSX 코드를 다시 실행하고 화면을 업데이트.
    const [isDropdownVisible, setDropdownVisible] = useState(false);
    const [isSignedIn, setSignedIn] = useState(false);
    const [email, setEmail] = useState('');

    useEffect(() => {
        const email = localStorage.getItem('email');
        setSignedIn(!!email);
        setEmail(email || '');
    }, []);

    const toggleDropdown = () => {
        setDropdownVisible(!isDropdownVisible);
    };

    const signOut = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('email');
        setEmail('');
        setSignedIn(!isSignedIn);
        setDropdownVisible(!isDropdownVisible);
    }

    return (
        <div id='my'>
            <div id='my-container' onClick={toggleDropdown}>
                <img src={my} alt='my' />
                <div id='my-contents-box' style={{ display: isDropdownVisible ? 'flex' : 'none' }}>
                    <ul className='my-contents' style={{ display: !isSignedIn ? 'block' : 'none' }}>
                        <li><Link to="/">Home</Link></li>
                        <li><Link to="/SignIn">Sign In</Link></li>
                        <li><Link to="/SignUp">Sign Up</Link></li>
                    </ul>
                    <ul className='my-contents' style={{ display: isSignedIn ? 'block' : 'none' }}>
                        <li><Link to="/">Home</Link></li>
                        <li>{email}</li>
                        <li onClick={signOut}>sign out</li>
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default My;