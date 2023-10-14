import React, { ReactNode } from 'react';
import Header from "./Header";
import Nav from "./Nav";
import Footer from "./Footer";

import '../stylesheets/common/common.css';
import '../stylesheets/layouts/defaultLayout.css';

interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = (props) => {
  return (
    <div>
      <Header />
      <Nav />

      <main>
        {props.children}
      </main>

      <Footer />
    </div>
  );
}

export default Layout;