import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import axios from "axios";
import { API_URL } from '../Const';
import ViewDatesOfDishes from './ViewDatesOfDishes';
import ProfileRestaurant from '../profile/ProfileRestaurant';

export default class RestaurantPage extends Component {

    constructor(props) {
        super(props);

        var savedResult = localStorage.getItem('savedResult') || false;

        this.state = {
            addResult: savedResult,
            restaurantName: '',
            restaurantAddress: ''
        };
    }

    componentDidMount() {
        axios.get(`${API_URL}/company/restaurant`)
        .then(response => {
            if (response.status === 204) {
                this.setState({addResult: false});
                localStorage.setItem('savedResult', false);
            } else {
                this.setState({
                    addResult: true,
                    restaurantName: response.data.name,
                    restaurantAddress: response.data.address
                });
                localStorage.setItem('savedResult', true);
            }
            
        
        })


    }


    render() {

        return (
            <div>
                <div className="row"> 
                <div className="col-md-12 text-right">
                    <Link className="nav-link" to="/profileRestaurantUpdate"><b> {this.state.restaurantName}</b>({this.state.restaurantAddress}) </Link>
                    </div>
                </div>
                
                {this.state.addResult && <ViewDatesOfDishes/>}
                {!this.state.addResult && <ProfileRestaurant history= {this.props.history} />}
            </div>
        );
    }
}
