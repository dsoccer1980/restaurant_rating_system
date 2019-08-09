import {Component} from 'react';
import axios from 'axios';
import {API_URL} from '../Const';

export default class Vote extends Component {

    componentDidMount() {
        axios.post(`${API_URL}/user/vote/restaurant/${this.props.match.params.id}/date/${this.props.match.params.date}`)
            .then(res => {
                this.props.history.push(`/restaurants/view`);
            });
    }

    render() {
        return (
            ""
        );
    }
}