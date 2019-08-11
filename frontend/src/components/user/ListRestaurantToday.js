import React, { Component } from 'react';
import axios from "axios";
import { API_URL } from '../Const';
import RestaurantMenu from './RestaurantMenu';
import ViewUserVotes from './ViewUserVotes';
import AuthenticationService from '../authentication/AuthenticationService';

export default class ListRestaurantToday extends Component {

    constructor(props) {
        super(props);
        this.tabRow = this.tabRow.bind(this);
        var savedRestaurantId = localStorage.getItem('SavedRestaurantId') || -1;
        this.state = {
            restaurants: [],
            currentDate: new Date().toISOString().slice(0, 10),
            restaurantId: savedRestaurantId,
            refreshState: true,
            votedRestaurantId: -1,
            votes: []
        };
    }



    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/user/restaurant`, {}, { })
            .then(response => {
                this.setState({ restaurants: response.data });
            })

        var today = new Date().toISOString().slice(0, 10);
        axios.get(`${API_URL}/user/vote/date/${today}`)
            .then(response => {
                this.setState({ votedRestaurantId: response.data });
            })
    }

    onClickDate = (id) => {
        this.setState({ restaurantId: id }, function () {
            localStorage.setItem('SavedRestaurantId', this.state.restaurantId);
            this.refreshState();
        });
    }

    onClickCancel = () => {
        AuthenticationService.setupAxiosInterceptors();
        axios.delete(`${API_URL}/user/vote`)
            .then(response => {
                this.setState({ votedRestaurantId: -1 });
            })
    }

    onClickVote = (e, id) => {
        e.preventDefault();
        AuthenticationService.setupAxiosInterceptors();
        axios.post(`${API_URL}/user/vote/restaurant/${id}/date/${this.state.currentDate}`)
            .then(response => {
              this.setState({ votedRestaurantId: response.data });
            });
            this.refreshState();
    }

    tabRow() {

        return this.state.restaurants.map(function (object, i) {
            return (
                <div className="row" key={i}>
                    <div className="col-md-6">
                        <li className={"list-group-item " + (this.state.restaurantId === object.id ? 'active' : 'inactive')}
                            onClick={this.onClickDate.bind(this, object.id)}><b>{object.name}</b>&nbsp;({object.address})</li>
                    </div>
                    <div className="col-md-6">
                        {+this.state.votedRestaurantId === +object.id ?
                            <div>  <span className="btn btn-success">Voted</span> &nbsp;&nbsp;&nbsp;
                          <button className="btn btn-danger" onClick={this.onClickCancel}>Cancel</button>
                            </div> :
                            <button onClick={(e) =>this.onClickVote(e, object.id)} className="btn btn-primary">Vote</button>
                        }
                    </div>
                </div>
            )
        }, this);
    }

    refreshState = () =>
        this.setState({ refreshState: !this.state.refreshState })


    render() {

        return (
            <div>
                <div><br /><br /></div>
                {this.state.restaurants.length === 0 && <p>Nobody has yet offered menu today.</p>}
                {this.state.restaurants.length !== 0 && <p>Restaurants, who offer menu today. Choose a restaurant to view its menu.</p>}

                <ul className="list-group">
                    {this.tabRow()}
                </ul>

                <div><br /><br /><br /><br /></div>

                {this.state.restaurantId !== -1 &&
                    <RestaurantMenu currentDate={this.state.currentDate} restaurantId={this.state.restaurantId} refresh={this.state.refreshState} />
                }
                <div><br /><br /><br /><br /></div>
                <ViewUserVotes />
            </div>
        );
    }
}
