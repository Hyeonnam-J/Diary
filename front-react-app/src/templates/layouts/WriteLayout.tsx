import { LayoutRouteProps } from "react-router-dom";
import My from "../fragments/My";

import '../../stylesheets/common/common.css';
import '../../stylesheets/common/layout.css';

const WriteLayout: React.FC<LayoutRouteProps> = (props) => {
    return (
        <div className="container">
            <My />
            <main className='main-write'>
                {props.children}
            </main>
        </div>
    );
}

export default WriteLayout;
