import React, { ReactNode, useState } from 'react';
import SignLayout from "../layouts/SignLayout";
import '../../stylesheets/pages/signUp.css';
import Layout from "../../stylesheets/modules/layout.module.css";
import Button from "../../stylesheets/modules/button.module.css";

const SignUp = () => {
  const requestSignUp =async () => {
    try{
      const userIddInput = document.querySelector('input[name="userId"]') as HTMLInputElement;
      const passwordInput = document.querySelector('input[name="password"]') as HTMLInputElement;

      const data = {
        userId: userIddInput.value,
        password: passwordInput.value,
        // name: '',
        // birthDate: '',
        // phoneNumber: '',
        // email: '',
      };

      const response = await fetch('http://localhost:8080/signUp', {
        method: 'POST',
        headers: {
          "Content-Type": 'application/json',
        },
        body: JSON.stringify(data),
      }); //response

      // if(response.ok){
      //   const data = await response.json();
      //   console.log(data);
      // }
    }catch (error){
      console.log('Error : ', error);
    }
  } // requestSignUp

  return (
    <SignLayout>
      <main id='signUpFrame'>
        <div id='signUp-greetings'>
          <h3>Sign Up</h3>
        </div>

        <div id='submitList'>
          <label>
            <p>ID</p>
            <input name='userId'></input>
            <span>asdf</span>
          </label>

          <label>
            <p>Password</p>
            <input name='password'></input>
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
          <button className={ Button.primary } onClick={ requestSignUp }>회원가입</button>
        </div>
      </main>
    </SignLayout>
  )
}

export default SignUp;