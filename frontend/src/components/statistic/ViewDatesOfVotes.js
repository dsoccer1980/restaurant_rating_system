import React, { Component } from 'react';
import axios from "axios";
import { API_URL } from '../Const';
import getFormattedDate from '../Const';
import ViewResultsOfVotesByDate from './ViewResultsOfVotesByDate';
import AuthenticationService from '../authentication/AuthenticationService';

export default class ViewDatesOfVotes extends Component {

    constructor(props) {
        super(props);
        this.onClickDate = this.onClickDate.bind(this);
        this.tabRow = this.tabRow.bind(this);
        var savedCurrentDate = localStorage.getItem('SavedCurrentDate') || new Date().toISOString().slice(0, 10);

        this.state = {
            dates: [],
            currentDate: savedCurrentDate,
            refreshDate: true
        };
    }

    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/vote/date`)
            .then(response => {
                this.setState({ dates: response.data });
            })
    }

    onClickDate = (i) => {
        this.setState({ currentDate: this.state.dates[i] }, function () {
            this.refreshDate();
            localStorage.setItem('SavedCurrentDate',  this.state.currentDate);
        });
    }

    tabRow() {
        return this.state.dates.map(function (date, i) {
            return <li className={"list-group-item " + (this.state.currentDate === date ? 'active' : 'inactive')}
                       key={i} onClick={this.onClickDate.bind(this, i)}>{getFormattedDate(date)}
                    </li>
        }, this);
    }

    refreshDate = () =>
        this.setState({ refreshDate: !this.state.refreshDate })


    render() {

        return (
            <div>
                <h4>Results of votes:</h4>
                <ul className="list-group" style={{width:'15%'}}>
                    {this.tabRow()}
                </ul>
                <ViewResultsOfVotesByDate chosenDate={this.state.currentDate} refresh={this.state.refreshDate} />
            </div>
        );
    }
}
