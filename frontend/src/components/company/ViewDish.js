import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import { API_URL } from '../Const';
import getFormattedDate from '../Const';
import AuthenticationService from '../authentication/AuthenticationService';


export default class ViewDish extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dishes: [],
            currentDate: this.props.currentDate,

        };

    }
    componentWillReceiveProps(props) {
        const { refresh } = this.props;
        if (props.refresh !== refresh) {
            this.setState({ currentDate: this.props.currentDate })
            AuthenticationService.setupAxiosInterceptors();
            axios.get(`${API_URL}/company/dish/date/${this.props.currentDate}`)
                .then(response => {
                    this.setState({ dishes: response.data });
                })
        }
    }

    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/company/dish/date/${this.props.currentDate}`)
            .then(response => {
                this.setState({ dishes: response.data });
            })
    }


    tabRow() {
        return this.state.dishes.map(function (obj, i) {
            return (
                <tr key={i}>
                    <td>
                        {obj.name}
                    </td>
                    <td>
                        {obj.price}
                    </td>
                    <td>
                        {obj.date >= new Date().toISOString().slice(0, 10) && <Link to={`/dish/edit/${obj.id}`} className="btn btn-primary">Edit</Link>}
                    </td>
                    <td>
                        {obj.date >= new Date().toISOString().slice(0, 10) && <Link to={`/dish/delete/${obj.id}`} className="btn btn-danger">Delete</Link>}
                    </td>
                </tr>)


        });
    }

    render() {

        const isCurrentDate = this.state.currentDate === new Date().toISOString().slice(0, 10) ? true : false;

        return (
            <div>
                {isCurrentDate && this.state.dishes.length !== 0 && <h3 align="center">Current day menu</h3>}
                {isCurrentDate && this.state.dishes.length === 0 && <h3 align="center">You haven't create yet today menu.</h3>}
                {!isCurrentDate && <h3 align="center">Menu for {getFormattedDate(this.state.currentDate)}</h3>}
                {this.state.dishes.length !== 0 &&
                    <div>
                        <h3 align="center">Dish List</h3>
                        <table className="table table-striped" style={{ marginTop: 20 }}>
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Price</th>
                                    <th colSpan="2">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.tabRow()}
                            </tbody>
                        </table>
                    </div>
                }
                <div>
                    <Link to={'/createDish'} className="nav-link">
                        <button className="btn btn-primary">Create</button>
                    </Link>
                </div>
            </div>
        );
    }
}
