import React from 'react';
import '../stylesheets/layouts/header.css';

const Header: React.FC = () => {
  return (
    <header>
      <div id='info'>
        <button>로그인</button>
        <button>회원가입</button>
      </div>
    </header>
  );
}

export default Header;