import React, { Component } from 'react'
import AuthenticationService from './AuthenticationService';

export default class Login extends Component {

    constructor(props) {
        super(props)

        this.state = {
            username: '',
            password: '',
            hasLoginFailed: false,
            showSuccessMessage: false
        }

        this.handleChange = this.handleChange.bind(this)
        this.loginClicked = this.loginClicked.bind(this)
    }

    handleChange(event) {
        this.setState(
            {
                [event.target.name]
                    : event.target.value
            }
        )
    }

    loginClicked() {
        AuthenticationService
            .executeJwtAuthenticationService(this.state.username, this.state.password)
            .then((response) => {
                AuthenticationService.registerSuccessfulLoginForJwt(this.state.username, response.data.token)
                AuthenticationService.setupRoles(response.data.token)
                    .then(() => {
                        if (AuthenticationService.getRoles().indexOf("USER") !== -1) {
                            this.props.history.push(`/restaurants/view`);
                        } else if (AuthenticationService.getRoles().indexOf("COMPANY") !== -1) {
                           this.props.history.push(`/restaurantPage`);
                        } else {
                            this.props.history.push(`/login`);
                        }
                    })
            })
            .catch(() => {
                this.setState({ showSuccessMessage: false })
                this.setState({ hasLoginFailed: true })
            })
    }

    clickUserButton = () => {
        this.setState({ username: 'user', password: 'password' })
    }

    clickCompanyButton = () => {
        this.setState({ username: 'company', password: 'password' })
    }

    render() {
        return (
            <div>
                <h1>Login</h1>
                <div className="container">
                    {this.state.hasLoginFailed && <div className="alert alert-warning">Invalid Credentials</div>}
                    {this.state.showSuccessMessage && <div>Login Sucessful</div>}
                    User Name: <input type="text" name="username" value={this.state.username} onChange={this.handleChange} />
                    Password: <input type="password" name="password" value={this.state.password} onChange={this.handleChange} />
                    <button className="btn btn-success" onClick={this.loginClicked}>Login</button>
                </div>
                <div><br/></div>
                <button className="btn btn-lg btn-info" onClick={this.clickUserButton}>User</button> &nbsp; &nbsp;
                <button className="btn btn-lg btn-info" onClick={this.clickCompanyButton}>Company</button>


            </div>
        )
    }
}
