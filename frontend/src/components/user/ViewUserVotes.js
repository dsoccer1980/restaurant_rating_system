import React, { Component } from 'react';
import axios from "axios";
import { API_URL } from '../Const';
import getFormattedDate from '../Const';
import AuthenticationService from '../authentication/AuthenticationService';

export default class ViewUserVotes extends Component {

    constructor(props) {
        super(props);
        this.tabRow = this.tabRow.bind(this);
        this.state = {
            votes: [],
            refreshState: true
        };
    }

    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/user/vote`)
            .then(response => {
                this.setState({ votes: response.data });
            })
    }


    tabRow() {
        return this.state.votes.map(function (object, i) {
            if (object.date !== new Date().toISOString().slice(0, 10)) {
                return (
                    <tr key={i}>
                        <td>
                            {getFormattedDate(object.date)}
                        </td>
                        <td>
                            {object.restaurant.name}
                        </td>
                    </tr>
                )
            } else {
                return (<tr key='10000'></tr>);
            }
        }, this);
    }

    refreshState = () =>
        this.setState({ refreshState: !this.state.refreshState })


    render() {

        return (
            <div>
                Your previous votes
                <table border='1'>
                    <tbody>
                        {this.tabRow()}
                    </tbody>
                </table>


            </div>
        );
    }
}
