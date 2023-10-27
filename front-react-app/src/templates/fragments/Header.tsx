import React from 'react';
import { Link } from 'react-router-dom';

import home from '../../assets/imgs/home.png'

import '../../stylesheets/common/common.css';
import '../../stylesheets/layouts/header.css';

const Header: React.FC = () => {
  return (
    <header>
      <div className='dropdown-container'>
        <img src={home} alt='home' />
        <ul className='dropdown-content'>
          {/* React-Router의 Link component를 통해 URL이 변경 */}
          <li><Link to="/SignIn">sign in</Link></li>
          <li><Link to="/SignUp">sign up</Link></li>
        </ul>
      </div>
    </header>
  );
}

export default Header;