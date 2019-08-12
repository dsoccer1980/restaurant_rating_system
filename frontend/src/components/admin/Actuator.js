import React, { Component } from 'react';
import axios from "axios";
import { API_URL } from '../Const';
import AuthenticationService from '../authentication/AuthenticationService';

export default class Actuator extends Component {

    constructor(props) {
        super(props);
        this.tabRow = this.tabRow.bind(this);
        this.state = {
            health: {},
            metrics: {},
        };
    }

    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.get(`${API_URL}/admin/actuator/health`)
            .then(response => {
                this.setState({ health: response.data });
            })
        axios.get(`${API_URL}/admin/actuator/metrics`)
            .then(response => {
                this.setState({ metrics: response.data });
            })

    }

    tabRow() {
        return Object.keys(this.state.health).map((item, i) => {
            return (
                <tr key={i}>
                    <td>{item}</td>
                    <td>{JSON.stringify(this.state.health[item])}</td>
                </tr>
            )
        })

    }

    tabRow2() {
        return Object.keys(this.state.metrics).map((item, i) => {
            return (
                <tr key={i}>
                    <td>{item}</td>
                    <td>{JSON.stringify(this.state.metrics[item])}</td>
                </tr>
            )
        })

    }



    render() {

        return (
            <div>
                <br></br>
                <h3>Health</h3>
                <div className="row">
                    <table className="table table-striped" style={{ marginTop: 20, width: '50%' }}>
                        <thead>
                        </thead>
                        <tbody>
                            {this.tabRow()}
                        </tbody>
                    </table>
                </div>
                <br></br>
                <h3>Metrics</h3>
                <div className="row">
                    <table className="table table-striped" style={{ marginTop: 20, width: '50%' }}>
                        <thead>
                        </thead>
                        <tbody>
                            {this.tabRow2()}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}
