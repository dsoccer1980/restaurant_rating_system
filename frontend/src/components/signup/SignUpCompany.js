import React from 'react'
import { API_URL } from '../Const'
import AuthenticationService from '../authentication/AuthenticationService';

export default class SignUpCompany extends React.Component  {

    state = {
        name: '',
        email: '',
        password: '',
        addResult: null,
        errorMessage: ''
    };

    handleChange = (e) => {
        const { id, value } = e.currentTarget;
        this.setState({ [id]: value })
    };

    validate = () => {
        const { name, email, password } = this.state;
        if (name.trim() && email.trim() && password.trim()){
            return true;
        }
        return false;
    };

    checkNameAvailability = (name) => {
        return fetch('/admin/user/' + name)
    }
    
    saveUser = (user) => {
        AuthenticationService.setupAxiosInterceptors();
        return fetch(`${API_URL}/company`, {
            method: 'post',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify(user)
        });
    }

    handleSubmit = (e) => {
        e.preventDefault();
        e.stopPropagation();

        const {name, email, password} = this.state;
        const user = {name, email, password};

        if (this.checkEmail(email)) {
            this.checkNameAndSave(user);
        }
    };

    checkNameAndSave = (user) => {
   
        this.saveUserToDB(user);
   
    };

    checkEmail = (email) => {
        const EMAIL_REGEX = RegExp('[^@ ]+@[^@ ]+\\.[^@ ]+');
        if (!EMAIL_REGEX.test(email)) {
            this.setState({errorMessage: 'Wrong e-mail'});
            return false;
        }
        return true;
    };

    saveUserToDB = (user) => {
        this.saveUser(user)
            .then(response => {
                if (response.status === 200) {
                    this.setState({addResult: true});
                    this.props.history.push("/login");
                } else {
                    this.setState({addResult: false});
                }
            })
    };

    render() {

        const {name, email, password, addResult, errorMessage} = this.state;

        return (
            <div>
              <h4>Company signup</h4>
                <form className='form-horizontal'>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <label>Username
                                <input id='name'
                                       className='form-control'
                                       type='text'
                                       value={name}
                                       required={true}
                                       onChange={this.handleChange}
                                />
                            </label>
                        </div>
                    </div>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <label>Email
                                <input id='email'
                                       className='form-control'
                                       type='email'
                                       value={email}
                                       required={true}
                                       onChange={this.handleChange}
                                />
                            </label>
                        </div>
                    </div>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <label>Password
                                <input id='password'
                                       className='form-control'
                                       type='password'
                                       value={password}
                                       required={true}
                                       onChange={this.handleChange}
                                />
                            </label>
                        </div>
                    </div>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <button
                                className='btn btn-primary submit_login'
                                onClick={this.handleSubmit}
                                disabled={!this.validate()}>
                                Add
                            </button>
                        </div>
                    </div>
                </form>
                {
                    addResult === true ?
                        <p className='goodMsg'>Registration was successful</p> :
                        addResult === false ?
                            <p className='badMsg'>Something was wrong...</p> :
                            null
                }
                {
                    errorMessage ? <p className='badMsg'>{errorMessage}</p> : null
                }
            </div>
        )
    }
}