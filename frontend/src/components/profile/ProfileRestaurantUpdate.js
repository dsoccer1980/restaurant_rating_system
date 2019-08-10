import React, { Component } from 'react';
import { API_URL } from '../Const'
import axios from 'axios';
import AuthenticationService from '../authentication/AuthenticationService';

export default class ProfileRestaurantUpdate extends Component {
    state = {
        id: '',
        name: '',
        address: '',
        addResult: null,
        errorMessage: ''
    };

    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/company/restaurant`)
            .then(response => {
                this.setState({
                    name: response.data.name,
                    address: response.data.address,
                    id: response.data.id

                });
            })
    }

    handleChange = (e) => {
        const { id, value } = e.currentTarget;
        this.setState({ [id]: value })
    };

    validate = () => {
        const { name, address } = this.state;
        if (name.trim() && address.trim()) {
            return true;
        }
        return false;
    };

    saveRestaurant = (restaurant) => {
        AuthenticationService.setupAxiosInterceptors();
        axios.put(`${API_URL}/company/restaurant`, JSON.stringify(restaurant), {
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            }
        });

    }

    handleSubmit = (e) => {
        e.preventDefault();
        const { id, name, address } = this.state;
        const restaurant = { id, name, address };

        AuthenticationService.setupAxiosInterceptors();
        axios.post(`${API_URL}/company/restaurant`, JSON.stringify(restaurant), {
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
        this.props.history.push('/restaurantPage');
    }


    render() {

        const { id, name, address, addResult, errorMessage } = this.state;

        return (
            <div>
                <h4>Restaurant profile</h4>
                <form className='form-horizontal'>
                    <input id='id'
                        type='hidden'
                        value={id}
                        required={true}
                    />
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <label>Restaurant name
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
                            <label>Address
                                <input id='address'
                                    className='form-control'
                                    type='text'
                                    value={address}
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