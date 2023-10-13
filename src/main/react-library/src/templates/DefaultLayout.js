import Footer from "./Footer"
import Header from "./Header"
import Navi from "./Navi"

import '../css/common/common.css';
import '../css/layouts/defaultLayout.css';

const Layout = (props) => {
  return (
    <div>
      <Header />
      <Navi />

      <main>
        {props.children}
      </main>

      <Footer />
    </div>
  )
}

export default Layout