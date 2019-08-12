import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import AuthenticationService from './authentication/AuthenticationService';

export default class MenuComponent extends Component {


    render() {
        const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
        const userName = AuthenticationService.getLoggedInUserName();
        const role = AuthenticationService.getRoles();

        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav mr-auto">
                        <li>
                            <Link className="nav-link" to="/statistic">Statistic</Link>
                        </li>
                        {!isUserLoggedIn &&
                            <li className="nav-item">
                                <Link to='/login' className="nav-link">login</Link>
                            </li>
                        }
                        {!isUserLoggedIn &&
                            <li className="nav-item">
                                <Link to='/signUpUser' className="nav-link">signUpUser</Link>
                            </li>
                        }
                        {!isUserLoggedIn &&
                            <li className="nav-item">
                                <Link to='/signUpCompany' className="nav-link">signUpCompany</Link>
                            </li>
                        }

                        {isUserLoggedIn && role.indexOf("USER") !== -1 &&
                            <li>
                                <Link className="nav-link" to="/restaurants/view">Home</Link>
                            </li>
                        }

                        {isUserLoggedIn && role.indexOf("COMPANY") !== -1 &&
                            <li>
                                <Link className="nav-link" to="/restaurantPage">Home</Link>
                            </li>
                        }

                        {isUserLoggedIn && role.indexOf("ADMIN") !== -1 &&
                            <li>
                                <Link className="nav-link" to="/admin/users">Home</Link>
                            </li>
                        }

                        {isUserLoggedIn &&
                            <li>
                                <Link className="nav-link" to="/user/profile">Profile</Link>
                            </li>
                        }

                        {isUserLoggedIn &&
                            <li>
                                <Link className="nav-link" to="/logout" onClick={AuthenticationService.logout}>Logout</Link>
                            </li>
                        }


                    </ul>
                </div>
                {isUserLoggedIn &&
                    <h4>Hi, {userName} !</h4>
                }
            </nav>

        )
    }
}
