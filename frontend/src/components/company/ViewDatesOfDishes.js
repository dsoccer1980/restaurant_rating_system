import React, { Component } from 'react';
import axios from "axios";
import { API_URL } from '../Const';
import getFormattedDate from '../Const';
import ViewDish from './ViewDish';
import AuthenticationService from '../authentication/AuthenticationService';

export default class ViewDatesOfDishes extends Component {

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
        axios.get(`${API_URL}/company/dish/date`)
            .then(response => {
                this.setState({ dates: response.data });
            })
    }

    onClickDate = (i) => {
        this.setState({ currentDate: this.state.dates[i] }, function () {
            this.refreshDate();
            localStorage.setItem('SavedCurrentDate', this.state.currentDate);
        });
    }

    tabRow() {
        return this.state.dates.map(function (object, i) {
            return <div key={i} className="col-md-2">
                      <button type="button" className={"list-group-item list-group-item-action " + (this.state.currentDate === object ? 'active' : 'inactive')}
                           onClick={this.onClickDate.bind(this, i)}> {getFormattedDate(object)}
                      </button>
                   </div>

        }, this);
    }

    refreshDate = () =>
        this.setState({ refreshDate: !this.state.refreshDate })


    render() {

        return (
            <div>
                {this.state.dates.length !== 0 && <p>Previous dates menu</p>}
                <div className="row">
                    {this.tabRow()}
                </div>
                <ViewDish currentDate={this.state.currentDate} refresh={this.state.refreshDate} />
            </div>
        );
    }
}
