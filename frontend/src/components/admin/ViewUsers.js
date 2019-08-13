import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import { API_URL } from '../Const';
import getFormattedDate from '../Const';
import AuthenticationService from '../authentication/AuthenticationService';


export default class ViewUsers extends Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
        };
    }
 
    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/admin/users`)
            .then(response => {
                this.setState({ users: response.data });
            })
            
    }

    onDeleteClick = (e, id) => {
        e.preventDefault();
        AuthenticationService.setupAxiosInterceptors();
        axios.delete(`${API_URL}/admin/user/${id}`)
            .then(res => {
            });
    }


    tabRow = () => {
        return this.state.users.map(function (obj, i) {

            return (
                <tr key={i}>
                    <td>
                        {obj.name}
                    </td>
                    <td>
                        {obj.email}
                    </td>
                    <td>
                        {getFormattedDate(obj.registrationTime)}
                    </td>
                    <td>
                        {obj.roles[0].name}
                    </td>
                    <td>
                        <Link to={`/admin/editUser/${obj.id}`} className="btn btn-primary">Edit</Link>
                    </td>
                    <td>
                        <Link to={`/admin/user/delete/${obj.id}`} className="btn btn-danger">Delete</Link>
                    </td>
                </tr>)
        }, this);
    }


    render() {
        return (
            <div>
                {this.state.users.length !== 0 &&
                    <div>
                        <h3 align="center">Users List</h3>
                        <table className="table table-striped" style={{ marginTop: 20 }}>
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Reg.date</th>
                                    <th>Role</th>
                                    <th colSpan="2">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.tabRow()}
                            </tbody>
                        </table>
                    </div>
                }
            </div>
        );
    }
}
