import WriteLayout from "../layouts/WriteLayout";
import '../../stylesheets/pages/write.css';
import Layout from "../../stylesheets/modules/layout.module.css";

const Write = () => {
    return (
        <WriteLayout>
            <main className={Layout.centerFrame}>글쓰기야
                
            </main>
        </WriteLayout>
    )
}
    
export default Write;