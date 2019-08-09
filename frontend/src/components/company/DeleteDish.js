import {Component} from 'react';
import axios from 'axios';
import {API_URL} from '../Const';

export default class DeleteDish extends Component {

    componentDidMount() {
        axios.delete(`${API_URL}/company/dish/${this.props.match.params.id}`)
            .then(res => {
                this.props.history.push('/restaurantPage');
            });
    }

    render() {
        return (
            ""
        );
    }
}