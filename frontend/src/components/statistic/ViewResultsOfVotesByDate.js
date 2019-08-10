import React, { Component } from 'react';
import axios from "axios";
import { API_URL } from '../Const';
import AuthenticationService from '../authentication/AuthenticationService';

export default class ViewResultsOfVotesByDate extends Component {

    constructor(props) {
        super(props);
        this.tabRow = this.tabRow.bind(this);

        this.state = {
            resultMap: {},
        };
    }

    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/vote/date/${this.props.chosenDate}`)
            .then(response => {
                this.setState({ resultMap: response.data });
            })
    }

    componentWillReceiveProps(props) {
        const { refresh } = this.props;
        if (props.refresh !== refresh) {
            axios.get(`${API_URL}/vote/date/${this.props.chosenDate}`)
                .then(response => {
                    this.setState({ resultMap: response.data });
                })
        }
    }

    tabRow() {
        return Object.keys(this.state.resultMap).map((item, i) => {
            return (
              <tr key={i}>
                <td>{item}</td>
                <td>{this.state.resultMap[item]}</td>
              </tr>
            )
        })

    }

    render() {

        return (
            <div className="row">
                <table className="table table-striped" style ={{ marginTop: 20, width:'50%' }}>
                    <thead>
                        <tr>
                            <th>Restaurant</th>
                            <th>Amount of votes</th>
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
