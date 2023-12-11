import React, { ReactNode, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import SignLayout from "../../layouts/SignLayout";
import { SERVER_IP } from "../../../Config";
import '../../../stylesheets/pages/sign/signUp.css';
import Layout from "../../../stylesheets/modules/layout.module.css";
import Button from "../../../stylesheets/modules/button.module.css";

const SignUp = () => {
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [userName, setUserName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [validations, setValidations] = useState({
        email: false,
        password: false,
        userName: false,
        phoneNumber: true,
    });

    const getValidationMessage = (name: string) => {
        switch (name) {
            case "email":
                if(!emailRegex.test(email)) return "Invalid email format";
                else return "Valid email";
            case "password":
                if (!/^\S+$/.test(password)) {
                    return "Password cannot contain spaces";
                } else if (/[^\x00-\x7F]/.test(password)) {
                    return "Password can contain only ASCII characters";
                } else if (!/[A-Z]/.test(password)) {
                    return "Password must contain at least one uppercase letter";
                } else if (!/[a-z]/.test(password)) {
                    return "Password must contain at least one lowercase letter";
                } else if (!/\d/.test(password)) {
                    return "Password must contain at least one digit";
                } else if (!/[@$!%*#?&]/.test(password)) {
                    return "Password must contain at least one special character";
                } else if (password.length < 8) {
                    return "Password must be at least 8 characters long";
                } else {
                    return "Valid password";
                }
            case "userName":
                if (/\p{P}/u.test(userName)) return "Name cannot contain special characters";
                else if (/\d/.test(userName)) return "Name cannot contain numbers";
                else if (/^\s|\s$/.test(userName)) return "Name cannot start or end with spaces";
                else if (!userName.trim()) return "Name is required";
                else return "Valid name";
            case "phoneNumber":
                if(phoneNumberRegex.test(phoneNumber)) return "";
                return "";
            default:
                return "";
        }
    };
    
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$/;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
    const userNameRegex = /^(?!\d)(?!\s)[\p{L}\d\s]*[\p{L}\d]$/u;
    const phoneNumberRegex = /.*/;

    let isValid = false;
    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;

        switch (name) {
            case "email":
                isValid = emailRegex.test(value);
                break;
            case "password":
                isValid = passwordRegex.test(value);
                break;
            case "userName":
                isValid = userNameRegex.test(value);
                break;
            case "phoneNumber":
                isValid = phoneNumberRegex.test(phoneNumber);
                break;
            default:
                break;
        }

        setValidations({
            ...validations,
            [name]: isValid,
        });

        switch (name) {
            case "email":
                setEmail(value);
                break;
            case "password":
                setPassword(value);
                break;
            case "userName":
                setUserName(value);
                break;
            case "phoneNumber":
                setPhoneNumber(value);
                break;
            default:
                break;
        }
    };

    const requestSignUp = async () => {
        for (const key in validations) {
            if (!validations[key as keyof typeof validations]) {
                
                if(key === 'userName') {
                    alert("check your name value");
                    return;
                }

                alert("check your "+key+" value");
                return;
            }
        }

        try{
            const emailInput = document.querySelector('input[name="email"]') as HTMLInputElement;
            const passwordInput = document.querySelector('input[name="password"]') as HTMLInputElement;
            const userNamedInput = document.querySelector('input[name="userName"]') as HTMLInputElement;
            const phoneNumberInput = document.querySelector('input[name="phoneNumber"]') as HTMLInputElement;

            const data = {
                email: emailInput.value,
                password: passwordInput.value,
                userName: userNamedInput.value,
                phoneNumber: phoneNumberInput.value,
            };

            const response = await fetch(SERVER_IP+'/signUp', {
            headers: {
                "Content-Type": 'application/json',
            },
            method: 'POST',
//             credentials: 'include',
            body: JSON.stringify(data),
            })
            .then(response => {
                if(response.ok) {
                    navigate('/');
                    alert("Registration is complete")
                }
            })
        }catch (error){
            console.log('Error : ', error);
        }
    } // requestSignUp

    return (
        <SignLayout>
            <div id='signUpFrame'>
                <div id='submitList'>
                    <label className={validations.email ? 'valid' : 'invalid'}>
                        <p>* Email</p>
                        <input name='email' value={email} onChange={handleInputChange}></input>
                        <span>{getValidationMessage('email')}</span>
                    </label>

                    <label className={validations.password ? 'valid' : 'invalid'}>
                        <p>* Password</p>
                        <input name='password' value={password} onChange={handleInputChange}></input>
                        <span>{getValidationMessage('password')}</span>
                    </label>

                    <label className={validations.userName ? 'valid' : 'invalid'}>
                        <p>* Name</p>
                        <input name='userName' value={userName} onChange={handleInputChange}></input>
                        <span>{getValidationMessage('userName')}</span>
                    </label>

                    <label className={validations.phoneNumber ? 'valid' : 'invalid'}>
                        <p>Phone number</p>
                        <input name='phoneNumber' value={phoneNumber} onChange={handleInputChange}></input>
                        <span>{getValidationMessage('phoneNumber')}</span>
                    </label>
                </div>
                <button id='terms' className={ Button.primaryOutline }>Terms of Service</button>
                <button id='submit-btn' className={ Button.primary } onClick={ requestSignUp }>Sign up</button>
            </div>
        </SignLayout>
    )
}

export default SignUp;