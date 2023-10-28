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
            <span>ID</span>
            <input></input>
            <span>asdf</span>
          </label>

          <label>
            <span>Password</span>
            <input></input>
            <span>asdf</span>
          </label>

          <label>
            <span>Name</span>
            <input></input>
            <span>asdf</span>
          </label>

          <label>
            <span>Birth date</span>
            <input></input>
            <span>asdf</span>
          </label>

          <label>
            <span>Phone number</span>
            <input></input>
            <span>fds</span>
          </label>

          <label>
            <span>Email</span>
            <input></input>
            <span>sdf</span>
          </label>
        </div>

        <div id='terms'>
          이용약관
        </div>

        <div id='submit-btn-box'>
          <button>회원가입</button>
        </div>
      </main>
    </SignLayout>
  )
}

export default SignUp;