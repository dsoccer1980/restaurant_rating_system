import React, { Component } from 'react';
import { API_URL } from '../Const'
import axios from 'axios';
import AuthenticationService from '../authentication/AuthenticationService';

export default class ProfileUser extends Component {
    state = {
        name: '',
        password: '',
        email: '',
        addResult: null,
        errorMessage: ''
    };

    componentDidMount() {
        axios.get(`${API_URL}/user`)
            .then(response => {
                this.setState({
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
        const { name, password, email } = this.state;
        const userProfile = { name, password, email };

        axios.put(`${API_URL}/user`, JSON.stringify(userProfile), {
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
        if (AuthenticationService.getRoles().indexOf("USER") !== -1) {
            this.props.history.push(`/restaurants/view`);
        } else if (AuthenticationService.getRoles().indexOf("COMPANY") !== -1) {
            this.props.history.push(`/restaurantPage`);
        }
    }



    render() {

        const { name, password, email, addResult, errorMessage } = this.state;

        return (
            <div>
                <h4>User profile</h4>
                <form className='form-horizontal'>
                    <div className='form-group'>
                        <div className='col-sm-10'>
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