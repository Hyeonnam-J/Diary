import SignLayout from "../layouts/SignLayout";
import '../../stylesheets/pages/signIn.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";

const SignIn = () => {
  return (
    <SignLayout>
      <div id='signInFrame' className={Layout.centerFrame}>

        <div id="signInFrame-inputs">
          <p>ID</p>
          <input type="text"></input>
          <p>Password</p>
          <input type="text"></input>
        </div>

        <div id='signInFrame-btns'>
          <button className={ Button.primary }>Sign In</button>
          <button className={ Button.primaryOutline }>Naver</button>
          <button className={ Button.primaryOutline }>Kakao</button>
        </div>

      </div>
    </SignLayout>
  )
}

export default SignIn;