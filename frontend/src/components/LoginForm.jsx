import { Formik, Field, Form } from 'formik';
import * as Yup from 'yup';
import {useNavigate} from "react-router-dom";
import {useState} from "react";

import {login} from "../api/authentiactionService.js";

function LoginForm() {
    const navigate = useNavigate();
    const [error, setError] = useState(null);
    const LoginSchema = Yup.object().shape({
        username: Yup.string().required("Required"),
        password: Yup.string().required('Required')
    });

    const handleSubmit = async (values, { resetForm }) => {
        const { username, password } = values;
        const success = await login(username, password, setError);

        if (success) {
            navigate('/home');
        } else {
            resetForm();
        }
    }

    return (
        <Formik
            initialValues={{ username: '', password: '' }}
            validationSchema={LoginSchema}
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
                        <label htmlFor="password">Password</label>
                        <Field name="password" type="password" id="password" />
                        {errors.password && touched.password ? <div className="error">{errors.password}</div> : null}
                    </div>

                    <button type="submit">Submit</button>
                    { error && <div>{error}</div> }
                </Form>
            )}
        </Formik>
    );
}

export default LoginForm;
