import React, { Component } from 'react';
import { API_URL } from '../Const'
import axios from 'axios';
import AuthenticationService from '../authentication/AuthenticationService';

export default class EditUser extends Component {
    state = {
        id: '', 
        name: '',
        password: '',
        email: '',
        addResult: null,
        errorMessage: ''
    };

    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/admin/user/${this.props.match.params.id}`)
            .then(response => {
                this.setState({
                    id: response.data.id,
                    name: response.data.name,
                    password: response.data.password,
                    email: response.data.email,
                });
            })
    }

    handleChange = (e) => {
        const { id, value } = e.currentTarget;
        this.setState({ [id]: value })
    };

    validate = () => {
        const { name, password, email } = this.state;
        if (name.trim() && password.trim() && email.trim()) {
            return true;
        }
        return false;
    };

    handleSubmit = (e) => {
        e.preventDefault();
        const { id, name, password, email } = this.state;
        const userProfile = { id, name, password, email };

        AuthenticationService.setupAxiosInterceptors();
        axios.put(`${API_URL}/admin/user`, JSON.stringify(userProfile), {
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.status === 200) {
                this.setState({ addResult: true });
            } else {
                this.setState({ addResult: false });
            }
        })
    };

    onCancelClick = (e) => {
        e.preventDefault();
            this.props.history.push(`/admin/users`);
    }



    render() {

        const { id, name, password, email, addResult, errorMessage } = this.state;

        return (
            <div>
                <h4>User profile</h4>
                <form className='form-horizontal'>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                             <input id='id'
                                    type='hidden'
                                    value={id}
                                    required={true}
                                />
                            <label>User name
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
                            <button
                                className='btn btn-primary submit_login'
                                onClick={this.handleSubmit}
                                disabled={!this.validate()}>
                                Save
                            </button> &nbsp;&nbsp;
                            <button type="button" className="btn btn-secondary" onClick={this.onCancelClick}>Cancel
                             </button>
                        </div>
                    </div>
                </form>
                {
                    addResult === true ?
                        <p className='goodMsg'>Saving was successful</p> :
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