import React from 'react';
import '../stylesheets/layouts/header.css';
import btn from '../stylesheets/modules/button.module.css';

const Header: React.FC = () => {
  return (
    <header>
      <div id='info'>
        <button className={btn.primary}>로그인</button>
        <button className={btn.primaryOutline}>회원가입</button>
      </div>
    </header>
  );
}

export default Header;