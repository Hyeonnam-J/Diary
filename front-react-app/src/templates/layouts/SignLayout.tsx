import { LayoutRouteProps } from "react-router-dom";
import Header from "../fragments/Header";

import '../../stylesheets/common/common.css';
import '../../stylesheets/common/layout.css';

const SignLayout: React.FC<LayoutRouteProps> = (props) => {
  return(
    <div className="container">
      <Header />

      <main>
        {props.children}
      </main>

    </div>
  );
}

export default SignLayout;
