import React from 'react';
import '../stylesheets/layouts/nav.css';

const Nav: React.FC = () => {
  return (
    <nav>
      <ul>
        <li><a href="#">메뉴 항목 1</a></li>
        <li><a href="#">메뉴 항목 2</a></li>
        <li><a href="#">메뉴 항목 3</a></li>
        <li><a href="#">메뉴 항목 4</a></li>
      </ul>
    </nav>
  );
}

export default Nav;