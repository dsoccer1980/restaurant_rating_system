export const API_URL = process.env.REACT_APP_SERVER_URL || 'http://localhost:8080';
export const ACCESS_TOKEN = 'accessToken';

export default function getFormattedDate(date) {
   return new Date(date).toLocaleDateString();
}

