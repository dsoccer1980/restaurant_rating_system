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
        this.setState({[event.target.name]: event.target.value
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
            <div className="container login-container" style={{marginTop:50}}>
                <div className="row">
                    <div className="col-md-4 login-form-1">
                        {this.state.hasLoginFailed && <div className="alert alert-warning">Invalid Credentials</div>}
                        {this.state.showSuccessMessage && <div>Login Sucessful</div>}
                        <h3 align="center">Login</h3>
                            <div className="form-group">
                                <input type="text" name="username" className="form-control" placeholder="Your Username *"  value={this.state.username} onChange={this.handleChange} />
                            </div>
                            <div className="form-group">
                                <input type="password" name="password" className="form-control" placeholder="Your Password *" value={this.state.password} onChange={this.handleChange} />
                            </div>
                            <div className="form-group">
                                <button className="btn btn-success" onClick={this.loginClicked}>Login</button>
                            </div>
                            <button className="btn btn-lg btn-info" onClick={this.clickUserButton}>User</button> &nbsp; &nbsp;
                             <button className="btn btn-lg btn-info" onClick={this.clickCompanyButton}>Company</button>
                    </div>
                </div>
                
            </div>
        )
    }
}
