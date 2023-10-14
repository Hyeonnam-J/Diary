import React from 'react';
import { Link } from 'react-router-dom';

import '../../stylesheets/layouts/header.css';
import btn from '../../stylesheets/modules/button.module.css';

const Header: React.FC = () => {
  return (
    <header>
      <div id='info'>
        {/* React-Router의 Link component를 통해 URL이 변경 */}
        <Link to="/Login" className={btn.primary}>로그인</Link>
        <Link to="/Registration" className={btn.primaryOutline}>회원가입</Link>
      </div>
    </header>
  );
}

export default Header;