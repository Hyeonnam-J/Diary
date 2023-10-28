import { LayoutRouteProps } from "react-router-dom";
import Header from "../fragments/Header";

import '../../stylesheets/common/common.css';
import '../../stylesheets/common/layout.css';

const SignLayout: React.FC<LayoutRouteProps> = (props) => {
  return(
    <div className="container">
      <Header />
      {/* 디폴트 레이아웃에서는 메인 태그까지 레이아웃에서 정의했지만,
      nav를 쓰지 않는 signLayout에서는 각 페이지마다 main의 css가 달라
      각 페이지에 정의 */}
      {props.children}
    </div>
  );
}

export default SignLayout;
