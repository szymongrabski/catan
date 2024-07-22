import { Formik, Field, Form } from 'formik';
import * as Yup from 'yup';
import {login} from "../api/authentiactionService.js";

function LoginForm() {
    const LoginSchema = Yup.object().shape({
        username: Yup.string().required("Required"),
        password: Yup.string().required('Required')
    });

    const handleSubmit = async (values) => {
        const { username, password } = values;
        const success = await login(username, password);

        if (success) {
            console.log('Zalogowano pomyślnie');
        } else {
            console.error('Logowanie nieudane');
        }
    }

    return (
        <Formik
            initialValues={{ username: '', password: '' }}
            validationSchema={LoginSchema}
            onSubmit={handleSubmit}
        >
            {({ errors, touched }) => (
                <Form>
                    <div>
                        <label htmlFor="username">Username</label>
                        <Field name="username" type="text" id="username" />
                        {errors.username && touched.username ? <div>{errors.username}</div> : null}
                    </div>

                    <div>
                        <label htmlFor="password">Password</label>
                        <Field name="password" type="password" id="password" />
                        {errors.password && touched.password ? <div>{errors.password}</div> : null}
                    </div>

                    <button type="submit">Submit</button>
                </Form>
            )}
        </Formik>
    );
}

export default LoginForm;
