import { useNavigate } from 'react-router-dom';
import SignLayout from "../layouts/SignLayout";
import '../../stylesheets/pages/signIn.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";

const SignIn = () => {
    const navigate = useNavigate();

    const requestSignIn = async () => {
        const emailInput = document.querySelector('input[name="email"]') as HTMLInputElement
        const passwordInput = document.querySelector('input[name="password"]') as HTMLInputElement

        const data = {
            email: emailInput.value,
            password: passwordInput.value,
        }

        const response = fetch('http://localhost:8080/signIn', {
            headers: {
                "Content-Type": 'application/json',
//                 "credentials": 'include',
            },
            method: 'POST',
//             credentials: 'include',
            body: JSON.stringify(data),
        })
        .then(response => {
            if(response.ok){
                const accessToken = response.headers.get('Authorization');
                localStorage.setItem('accessToken', accessToken || '')
                console.log(accessToken);
                navigate('/');
            }
        })
        .catch(error => {
            alert(error);
        })
    }
    return (
        <SignLayout>
            <main id='signInFrame' className={Layout.centerFrame}>

            <div id="signInFrame-inputs">
                <p>Email</p>
                <input type="text" name="email"></input>
                <p>Password</p>
                <input type="text" name="password"></input>
            </div>

            <div id='signInFrame-btns'>
                <button className={ Button.primary } onClick={ requestSignIn }>Sign In</button>
                <button className={ Button.primaryOutline }>Naver</button>
                <button className={ Button.primaryOutline }>Kakao</button>
            </div>

            </main>
        </SignLayout>
    )
}

export default SignIn;