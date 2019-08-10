import axios from 'axios';
import { API_URL, ACCESS_TOKEN } from '../Const'


export const USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser';
export const IS_USER_ADMIN = 'false';
export const ROLES = 'roles';

class AuthenticationService {

    executeJwtAuthenticationService(username, password) {
        return axios.post(`${API_URL}/authenticate`, {
            username,
            password
        })
    }

    registerSuccessfulLoginForJwt = (username, token) => {
        sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, username);
        localStorage.setItem(ACCESS_TOKEN, token);
        this.setupAxiosInterceptors();
    }

    setupRoles = (token) => {
        return axios.get(`${API_URL}/role`, {}, {
            headers: {
                Authorization: 'Bearer ' + token
            }
        }
        ).then(response => {
            sessionStorage.setItem(ROLES, response.data)
            sessionStorage.setItem(IS_USER_ADMIN, response.data)
        });
    }

    getRoles = () => {
        return sessionStorage.getItem(ROLES);
    }

    isUserAdmin() {
        let isAdmin = sessionStorage.getItem(IS_USER_ADMIN);
        if (isAdmin === null) return false;
        return isAdmin;
    }

    logout() {
        sessionStorage.clear();
        localStorage.clear();
        window.location.href = '/login';
    }

    isUserLoggedIn() {
        let user = sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME)
        if (user === null) return false
        return true
    }

    getLoggedInUserName() {
        let user = sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME)
        if (user === null) return ''
        return user
    }

    setupAxiosInterceptors() {
        const token = 'Bearer ' + localStorage.getItem(ACCESS_TOKEN);
        axios.interceptors.request.use(
            (config) => {
                if (this.isUserLoggedIn()) {
                    config.headers.authorization = token;
                }
                return config
            }
        )
    }
}

export default new AuthenticationService()