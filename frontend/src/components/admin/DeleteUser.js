import {Component} from 'react';
import axios from 'axios';
import {API_URL} from '../Const';
import AuthenticationService from '../authentication/AuthenticationService';

export default class DeleteUser extends Component {

    componentDidMount() {
        AuthenticationService.setupAxiosInterceptors();
        axios.delete(`${API_URL}/admin/user/${this.props.match.params.id}`)
        .then(res => {
            this.props.history.push('/admin/users');
        });
    }

    render() {
        return (
            ""
        );
    }
}