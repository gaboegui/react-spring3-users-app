import axios from "axios";

/**
 * The interceptor allows to replace all requests
 * 
 * API to replace the call in userService.js: 
 *      const response = await axios.post(
 *           BASE_WS_URL, { username, email, password, admin },  config_headers() );
 * 
 * The new way of the call : 
 *      const response = await usersApi.post('', { username, email, password, admin } );
 */

const usersApi = axios.create({
    baseURL: `${import.meta.env.VITE_API_BASE_URL}/users`
});

usersApi.interceptors.request.use( config => {
    config.headers = {
        ...config.headers,
        'Authorization': sessionStorage.getItem("token")
}
    return config;
});

export default usersApi;