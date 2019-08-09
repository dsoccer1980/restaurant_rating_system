import React, {Component} from 'react';
import axios from "axios";
import {API_URL} from '../Const';

export default class EditDish extends Component {
    constructor(props) {
        super(props);
        
        this.state = {
            id: '',
            name: '',
            price: 0,
            date: ''
        }
    }

    componentDidMount() {
        axios.get(`${API_URL}/company/dish/${this.props.match.params.id}`)
            .then(response => {
                this.setState({
                    id: response.data.id,
                    name: response.data.name,
                    price: response.data.price,
                    date: response.data.date
                });
            })
    }

    onChangeName = (e) => {
        this.setState({name: e.target.value})
    }
    onChangeDate = (e) => {
        this.setState({date: e.target.value})
    }
    onChangePrice = (e) => {
        this.setState({price: e.target.value})
    }

    onCancelClick = (e) => {
        e.preventDefault();
        this.props.history.push('/dishes');
    }

    onSubmit = (e) => {
        e.preventDefault();
        const obj = {
            id: this.state.id,
            name: this.state.name,
            price: this.state.price,
            date: this.state.date
        };
        axios.put(`${API_URL}/company/dish`, JSON.stringify(obj), {
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                this.props.history.push('/restaurantPage');
            });
    }

    render() {
        return (
            <div style={{marginTop: 10}}>
                <h3 align="center">Update Dish</h3>
                <form onSubmit={this.onSubmit}>
                    <div className="form-group">
                        <input type="hidden" className="form-control" value={this.state.id}/>
                    </div>
                    <div className="form-group">
                        <label>Dish Name: </label>
                        <input type="text" className="form-control" name="name" required value={this.state.name} onChange={this.onChangeName}/>
                        <input type="text" className="form-control" name="price" required value={this.state.price} onChange={this.onChangePrice} />
                        Date: {this.state.date}
                    </div>
                    <div className="row">
                        <div className="form-group">
                            <input type="submit" value="Update" className="btn btn-primary"/> &nbsp;
                            <button type="button" className="btn btn-secondary" onClick={this.onCancelClick}>Cancel
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        )
    }
}