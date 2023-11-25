import WriteLayout from "../layouts/WriteLayout";
import '../../stylesheets/pages/write.css';
import Layout from "../../stylesheets/modules/layout.module.css";

const Write = () => {
    return (
        <WriteLayout>
            <div id='writeFrame'>
                <div id='write-space'></div>
                <div id='write-header'></div>
                <div id='write-title'></div>
                <div id='write-custom'></div>
                <div id='write-content'></div>
            </div>
        </WriteLayout>
    )
}
    
export default Write;