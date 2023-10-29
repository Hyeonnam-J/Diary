import SignLayout from "../layouts/SignLayout";
import '../../stylesheets/pages/signUp.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";

const SignUp = () => {
  return (
    <SignLayout>
      <main id='signUpFrame'>
        <div id='signUp-greetings'>
          <h3>Sign Up</h3>
        </div>

        <div id='submitList'>
          <label>
            <p>ID</p>
            <input></input>
            <span>asdf</span>
          </label>

          <label>
            <p>Password</p>
            <input></input>
            <span>asdf</span>
          </label>

          <label>
            <p>Name</p>
            <input></input>
            <span>asdf</span>
          </label>

          <label>
            <p>Birth date</p>
            <input></input>
            <span>asdf</span>
          </label>

          <label>
            <p>Phone number</p>
            <input></input>
            <span>fds</span>
          </label>

          <label>
            <p>Email</p>
            <input></input>
            <span>sdf</span>
          </label>
        </div>

        <div id='terms'>
          이용약관
        </div>

        <div id='submit-btn-box'>
          <button className={ Button.primary }>회원가입</button>
        </div>
      </main>
    </SignLayout>
  )
}

export default SignUp;