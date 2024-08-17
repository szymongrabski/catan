import { Formik, Field, Form } from 'formik';
import * as Yup from 'yup';
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import {register} from "../api/authentiactionService.js";

function RegisterForm() {
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    const RegisterSchema = Yup.object().shape({
        username: Yup.string().required("Required").min(4, "Username must be at least 4 characters"),
        email: Yup.string().email("Invalid email address").required("Required"),
        password: Yup.string().required('Required').min(6, 'Password must be at least 6 characters'),
        confirmPassword: Yup.string()
            .oneOf([Yup.ref('password'), null], 'Passwords must match')
            .required('Required')
    });

    const handleSubmit = async (values, { resetForm }) => {
        const { username, email, password } = values;
        const success = await register(username, email, password, setError);

        if (success) {
            navigate('/home');
        } else {
            resetForm();
        }
    }

    return (
        <Formik
            initialValues={{ username: '', email: '', password: '', confirmPassword: '' }}
            validationSchema={RegisterSchema}
            onSubmit={handleSubmit}
        >
            {({ errors, touched }) => (
                <Form className="form">
                    <div className="field-container">
                        <label htmlFor="username">Username</label>
                        <Field name="username" type="text" id="username" />
                        {errors.username && touched.username ? <div className="error">{errors.username}</div> : null}
                    </div>

                    <div className="field-container">
                        <label htmlFor="email">Email</label>
                        <Field name="email" type="email" id="email" />
                        {errors.email && touched.email ? <div className="error">{errors.email}</div> : null}
                    </div>

                    <div className="field-container">
                        <label htmlFor="password">Password</label>
                        <Field name="password" type="password" id="password" />
                        {errors.password && touched.password ? <div className="error">{errors.password}</div> : null}
                    </div>

                    <div className="field-container">
                        <label htmlFor="confirmPassword">Confirm Password</label>
                        <Field name="confirmPassword" type="password" id="confirmPassword" />
                        {errors.confirmPassword && touched.confirmPassword ? <div className="error">{errors.confirmPassword}</div> : null}
                    </div>

                    <button type="submit">Submit</button>
                    { error && <div className="error">{error}</div> }
                </Form>
            )}
        </Formik>
    );
}

export default RegisterForm;
