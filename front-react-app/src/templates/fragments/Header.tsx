import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import home from '../../assets/imgs/home.png'

import '../../stylesheets/common/common.css';
import '../../stylesheets/fragments/header.css';

const Header: React.FC = () => {
  // React Hook인 useState를 사용하여 새로운 상태 변수를 생성
  // 변수명 isDropdownVisible, 초깃값 false.
  // 이 변수를 변경하기 위한 함수 setDropdownVisible.
  const [isDropdownVisible, setDropdownVisible] = useState(false);

  const toggleDropdown = () => {
    // setDropdownVisible(true)를 호출하면 isDropdownVisible 값이 true
    // setDropdownVisible(false)를 호출하면 isDropdownVisible 값이 false    
    setDropdownVisible(!isDropdownVisible);
  };

  return (
    <header>
      <div id='header-container' onClick={toggleDropdown}>
        <img src={home} alt='home' />
        <div id='header-contents-box' style={{ display: isDropdownVisible ? 'flex' : 'none' }}>
          <ul id='header-contents'>
            {/* React-Router의 Link component를 통해 URL이 변경 */}
            <li><Link to="/SignIn">Sign In</Link></li>
            <li><Link to="/SignUp">Sign Up</Link></li>
          </ul>
        </div>
      </div>
    </header>
  );
}

export default Header;