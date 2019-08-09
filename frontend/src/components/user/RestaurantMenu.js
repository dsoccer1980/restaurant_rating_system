import React, { Component } from 'react';
import axios from "axios";
import { API_URL } from '../Const';
import getFormattedDate from '../Const';

export default class RestaurantMenu extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dishes: [],
            currentDate: this.props.currentDate,
            restaurantId: this.props.restaurantId
        };

    }
    componentWillReceiveProps(props) {
        const { refresh } = this.props;
        if (props.refresh !== refresh) {
            this.setState({ currentDate: this.props.currentDate })
            axios.get(`${API_URL}/user/dish/restaurant/${this.props.restaurantId}/date/${this.props.currentDate}`)
                .then(response => {
                    this.setState({ dishes: response.data });
                })
        }
    }

    componentDidMount() {
        axios.get(`${API_URL}/user/dish/restaurant/${this.props.restaurantId}/date/${this.props.currentDate}`)
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
                        {getFormattedDate(obj.date)}
                    </td>
                </tr>
            )
        });
    }

    render() {

        return (
            <div>
                <h3 align="center">Dish List</h3>
                <table className="table table-striped" style={{ marginTop: 20 }}>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.tabRow()}
                    </tbody>
                </table>
            </div>
        );
    }
}
