import React from 'react';
import { Link } from 'react-router-dom';

import menu from '../../assets/imgs/menu.png'

import '../../stylesheets/common/common.css';
import '../../stylesheets/fragments/nav.css';

const Nav: React.FC = () => {
  return (
    <nav>
      <div id='nav-container'>
        <img src={menu} alt='menu' />
        <ul id='nav-contents'>
          <li><Link to="/Menu1">Menu1</Link></li>
          <li><Link to="/Menu2">Menu2</Link></li>
          <li><Link to="/Menu3">Menu3</Link></li>
          <li><Link to="/Menu4">Menu4</Link></li>
        </ul>
      </div>
    </nav>
  );
}

export default Nav;