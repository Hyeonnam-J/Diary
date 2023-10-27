import React, { ReactNode, useState } from 'react';
import { Link } from 'react-router-dom';

import Header from "../fragments/Header";
import Nav from "../fragments/Nav";

import '../../stylesheets/common/common.css';
import '../../stylesheets/layouts/defaultLayout.css';

interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = (props) => {
  const [isNavOpen, setNavOpen] = useState(false);

  const receiveNavClick = () => {
    setNavOpen(!isNavOpen);
  };

  const navWidth = 'var(--nav-contents-width)'
  const navWidthMinus = 'var(--nav-contents-width-minus)'

  return (
    <div id='container'>
      {/* nav-contents html 요소를 Nav.tsx에서 제어하기에는 코드가 복잡해짐
      DefaultLayout에 html 요소를 넣고 display 요소를 제어하는 것이 간편 */}
      <ul id='nav-contents' style={{ left: isNavOpen ? '0' : navWidthMinus }}>
        <li><Link to="/Menu1">Menu1</Link></li>
        <li><Link to="/Menu2">Menu2</Link></li>
        <li><Link to="/Menu3">Menu3</Link></li>
        <li><Link to="/Menu4">Menu4</Link></li>
      </ul>

      <main style={{ marginLeft: isNavOpen ? navWidth : '0' }}>
        <Header />
        <Nav navClick={receiveNavClick} />

        <section>
          <button>버튼 입니다</button>
          {props.children}
        </section>
      </main>

    </div>
  );
}

export default Layout;