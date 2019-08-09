import React, { Component } from 'react';
import axios from 'axios';
import { API_URL } from '../Const';

export default class AddDish extends Component {
    constructor(props) {
        super(props);

        this.state = {
            name: '',
            price: 0,
            date: new Date().toISOString().slice(0, 10),
        }
    }

    onChangeName = (e) => {
        this.setState({ name: e.target.value })
    }
    onChangeDate = (e) => {
        if (e.target.value < new Date().toISOString().slice(0, 10)) {
            this.setState({ date: this.state.date })
        } else {
            this.setState({ date: e.target.value })
        }
    }
    onChangePrice = (e) => {
        this.setState({ price: e.target.value })
    }

    onCancelClick = (e) => {
        e.preventDefault();
        this.props.history.push('/dishes');
    }

    onSubmit = (e) => {
        e.preventDefault();
        if (this.state.date < new Date().toISOString().slice(0, 10)) {
            alert("You can create dish only for today and for future day")
        } else {

            const obj = {
                name: this.state.name,
                price: this.state.price,
                date: this.state.date,
            };
            axios.post(`${API_URL}/company/dish`, JSON.stringify(obj), {
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                }
            })
                .then(res => {
                    this.props.history.push('/restaurantPage');
                });
        }
    }

    render() {
        return (
            <div style={{ marginTop: 10 }}>
                <h3>Add new dish </h3>
                <form onSubmit={this.onSubmit}>
                    <div className="form-group">
                        <input type="hidden" name="restaurant_id" value={this.state.restaurantId} />
                        <label>Data </label>
                        <input type="date" name="date" required value={this.state.date} onChange={this.onChangeDate} className="form-control" />
                        <label>Name </label>
                        <input type="text" name="name" required value={this.state.name} onChange={this.onChangeName} className="form-control" />
                        <label>Price </label>
                        <input type="text" name="price" required value={this.state.price} onChange={this.onChangePrice} className="form-control" />
                    </div>
                    <div className="row">
                        <div className="form-group">
                            <input type="submit" name="submit" value="submit" className="btn btn-primary" /> &nbsp;&nbsp;
                            <button type="button" className="btn btn-secondary" onClick={this.onCancelClick}>Cancel
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        )
    }
}

